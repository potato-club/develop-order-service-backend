package com.server.dos.service;

import com.server.dos.Enum.OrderState;
import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.*;
import com.server.dos.entity.*;
import com.server.dos.entity.user.User;
import com.server.dos.exception.custom.OrderException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.mapper.ImageMapper;
import com.server.dos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.server.dos.Enum.OrderState.*;
import static com.server.dos.mapper.OrderMapper.INSTANCE;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderFileRepository fileRepository;
    private final OrderDetailRepository detailRepository;
    private final OrderLikeRepository likeRepository;
    private final OrderImageRepository imageRepository;
    private final S3Service uploadService;
    private final JwtProvider jwtProvider;

    @Transactional
    public List<OrderResponseDto> getAllOrder() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(INSTANCE::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<MeetingDateDto> getAllOrderMeeting() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(INSTANCE::toMeeting).map(MeetingDateDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderDetailDto getOrderDetail(Long orderId) {
        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.BAD_REQUEST, "Order is not exist."));
        return INSTANCE.toDetailDto(detail);
    }

    // 로그인 구현 후 my order 가져오기도 추가
    @Transactional
    public Page<OrderDetailListDto> getOrderDetailList(String state, int page) {
        Page<OrderDetail> detailPaging;
        PageRequest pageRequest = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "id"));
        if (state.equals("complete")) {
            detailPaging = detailRepository.findOrderDetailsByStateOrderByIdDesc(COMPLETE, pageRequest);
        } else {
            detailPaging = detailRepository.findOrderDetailsByStateOrderByIdDesc(WORKING, pageRequest);
        }
        Page<OrderDetailListDto> detailDtoPaging = detailPaging.map(INSTANCE::toDetailListDto)
                .map(dto -> addThumbnail(dto));

        return detailDtoPaging;
    }

    // 메인 페이지 OrderList 정보 가져오기 (완료된 발주 중 좋아요 높은 순 5개)
    // 추후에 쿼리에서 COMPLETE 가져오게 변경
    @Transactional
    public List<OrderMainDto> getMainOrders() {
        List<OrderDetail> details = detailRepository.findTop5ByOrderByLikesDesc();
        return details.stream().filter(o -> o.getState().equals(COMPLETE)).map(INSTANCE::toDetailListDto)
                .map(dto -> addThumbnail(dto))
                .map(OrderMainDto::new)
                .collect(Collectors.toList());
    }

    // OrderDetail 좋아요 추가
    @Transactional
    public String orderLike(String token, Long orderId) {
        OrderDetail detail = detailRepository.findCompleteById(orderId);
        if(detail == null) {
            throw new OrderException(ErrorCode.BAD_REQUEST, "존재하지 않는 완료된 발주입니다");
        }
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        OrderLike like = likeRepository.findByOrderDetailAndUser(detail, user);
        if(like == null) {
            detail.setLikes(detail.getLikes() + 1);
            OrderLike orderLike = new OrderLike(detail, user);
            likeRepository.save(orderLike);
            return "좋아요 완료";
        } else {
            like.unLikeOrder(detail);
            likeRepository.delete(like);
            return "좋아요 취소";
        }
    }

    @Transactional
    public OrderDetailListDto addThumbnail(OrderDetailListDto dto) {
        OrderDetail detail = detailRepository.findById(dto.getId())
                .orElseThrow(() -> new OrderException(ErrorCode.BAD_REQUEST, "Order is not exist."));

        if (detail.getImages().size() != 0) {
            OrderImage image = detail.getImages().get(0);
            dto.setThumbnail(ImageMapper.INSTANCE.toDto(image));
        }
        return dto;
    }

    @Transactional
    public void createOrder(String token, List<MultipartFile> files, OrderRequestDto orderDto) {
        Order order;
        if(orderRepository.findBySiteName(orderDto.getSiteName()).isPresent()) {
            throw new OrderException(ErrorCode.CONFLICT, "이미 존재하는 sitename 입니다");
        }
        try {
            Order o = orderDto.toEntity();
            o.setUser(userRepository.findByEmail(jwtProvider.getUid(token)));
            order = orderRepository.save(o);
        } catch (DataIntegrityViolationException ex) {
            throw new OrderException(ErrorCode.CONFLICT, "Meeting 시간 중복입니다.");
        }

        if(files != null) {
            files.stream()
                    .map(uploadService::upload)
                    .map(url -> saveFile(order, url))
                    .collect(Collectors.toList());
        }

        OrderDetail detail = OrderDetail.builder()
                .id(order.getId())
                .state(WORKING)
                .order(order)
                .likes(0)
                .build();

        detailRepository.save(detail);
    }

    @Transactional
    public void updateOrderDetail(Long orderId, List<MultipartFile> images, OrderDetailRequestDto requestDto) {
        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.BAD_REQUEST, "Order is not exist."));

        if (images != null) updateImage(detail, images);
        if (requestDto != null) detail.update(requestDto);
    }

    @Transactional
    public void updateImage(OrderDetail detail, List<MultipartFile> images) {
        if (detail.getImages().size() != 0) {
            for (OrderImage image : detail.getImages()) {
                uploadService.delete(image.getImageName());
            }
            imageRepository.deleteMainImagesByOrderDetail(detail);
        }

        images.stream()
                .map(uploadService::upload)
                .map(url -> saveImage(detail, url))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    private OrderFile saveFile(Order order, String url) {
        OrderFile orderFile = OrderFile.builder()
                .s3Url(url)
                .fileName(StringUtils.getFilename(url))
                .order(order)
                .build();
        return fileRepository.save(orderFile);
    }

    private OrderImage saveImage(OrderDetail detail, String url) {
        OrderImage orderImage = OrderImage.builder()
                .imageUrl(url)
                .imageName(StringUtils.getFilename(url))
                .orderDetail(detail)
                .build();
        return imageRepository.save(orderImage);
    }

    public Boolean checkSiteNameDuplicate(String siteName) {
        Optional<Order> order = orderRepository.findBySiteName(siteName);
        if(order.isEmpty()) {
            return false;
        }
        return true;
    }
}
