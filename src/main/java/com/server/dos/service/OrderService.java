package com.server.dos.service;

import com.server.dos.Enum.OrderState;
import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.*;
import com.server.dos.entity.*;
import com.server.dos.entity.user.User;
import com.server.dos.exception.custom.AdminException;
import com.server.dos.exception.custom.OrderException;
import com.server.dos.exception.custom.UserException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.mapper.FileMapper;
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
import java.util.stream.Collectors;

import static com.server.dos.Enum.OrderState.*;
import static com.server.dos.entity.user.Role.*;
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
    public Page<OrderListResponseDto> getOrdersByState(String token, int page, boolean checked) {
        if (!checkAdmin(token)) throw new AdminException(ErrorCode.UNAUTHORIZED, "회원은 조회 불가능합니다.");
        PageRequest request = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Orders> all;
        if(!checked) {
            all = orderRepository.findByOrderDetailState(request, CHECK);
        } else {
            all = orderRepository.findByOrderDetailWorking(request);
        }
        return all.map(INSTANCE::toListResponse);
    }

    @Transactional
    public OrderResponseDto getOrder(String token, Long orderId) {
        if (!checkAdmin(token)) throw new AdminException(ErrorCode.UNAUTHORIZED, "회원은 조회 불가능합니다.");
        Orders orders = orderRepository.findById(orderId).orElseThrow();
        OrderResponseDto dto = INSTANCE.toResponse(orders);
        List<OrderFile> files = fileRepository.findByOrders(orders);
        if(!files.isEmpty()) {
            List<FileDto> fileDtoList = files.stream().map(FileMapper.INSTANCE::toDto).collect(Collectors.toList());
            dto.setFiles(fileDtoList);
        }
        return dto;
    }

    @Transactional
    public List<MeetingDateDto> getAllOrderMeeting() {
        List<Orders> all = orderRepository.findAll();
        return all.stream().map(INSTANCE::toMeeting).map(MeetingDateDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderDetailDto getOrderDetail(Long orderId) {
        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.NOT_FOUND, "Order is not exist."));

        return INSTANCE.toDetailDto(detail);
    }

    @Transactional
    public Page<OrderDetailListDto> getOrderDetailList(String state, String sort, int page) {
        Page<OrderDetail> detailPaging;
        PageRequest pageRequest;

        if(sort.equals("likes")) {
            pageRequest = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "likes")
                    .and(Sort.by(Sort.Direction.DESC, "createdDate")));
        } else if (sort.equals("rating") && state.equals("complete")) {
            pageRequest = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "rating")
                    .and(Sort.by(Sort.Direction.DESC, "createdDate")));
        } else {
            pageRequest = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "createdDate"));
        }

        if (state.equals("complete")) {
            detailPaging = detailRepository.findCompletedDetails(pageRequest, COMPLETED);
        } else {
            detailPaging = detailRepository.findNotCompletedDetails(pageRequest, COMPLETED);
        }

        return detailPaging.map(INSTANCE::toDetailListDto).map(this::addThumbnail);
    }

    @Transactional
    public Page<OrderDetailListDto> getOrderDetailListByToken(String token, int page) {
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        PageRequest pageRequest = PageRequest.of(page - 1, 4,
                Sort.by(Sort.Direction.DESC, "state"));
        Page<OrderDetail> detailPaging =
                detailRepository.findOrderDetailsByUserIdOrderByIdDesc(user.getId(), pageRequest);
        Page<OrderDetailListDto> detailDtoPaging = detailPaging.map(INSTANCE::toDetailListDto)
                .map(this::addThumbnail);

        return detailDtoPaging;
    }

    @Transactional
    public Page<OrderDetailListDto> getLikeOrderDetailListByToken(String token, int page) {
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        PageRequest pageRequest = PageRequest.of(page - 1, 4,
                Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<OrderDetail> detailPaging = likeRepository.findLikedOrderDetailsByUser(user, pageRequest);
        Page<OrderDetailListDto> detailDtoPaging = detailPaging.map(INSTANCE::toDetailListDto)
                .map(this::addThumbnail);

        return detailDtoPaging;
    }

    @Transactional
    public List<OrderMainDto> getMainOrders() {
        List<OrderDetail> details = detailRepository.findTop5ByOrderByLikesDesc();
        return details.stream().filter(o -> o.getState().equals(COMPLETED)).map(INSTANCE::toDetailListDto)
                .map(this::addThumbnail)
                .map(OrderMainDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public String orderLike(String token, Long orderId) {
        if (checkAdmin(token)) throw new AdminException(ErrorCode.BAD_REQUEST, "관리자는 추천할 수 없습니다.");
        OrderDetail detail = detailRepository.findCompleteById(orderId);
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        if (user == null) throw new UserException(ErrorCode.BAD_REQUEST, "존재하지 않는 유저입니다.");
        if (detail == null) throw new OrderException(ErrorCode.BAD_REQUEST, "존재하지 않는 완료된 발주입니다");
        if (user.getId() == detail.getUserId()) throw new OrderException(ErrorCode.BAD_REQUEST,
                "본인의 발주는 추천할 수 없습니다.");
        OrderLike like = likeRepository.findByOrderDetailAndUser(detail, user);
        if (like == null) {
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
        Orders orders;
        if (orderRepository.findBySiteName(orderDto.getSiteName()) != null) {
            throw new OrderException(ErrorCode.CONFLICT, "이미 존재하는 sitename 입니다");
        }
        try {
            Orders o = orderDto.toEntity();
            o.setUser(userRepository.findByEmail(jwtProvider.getUid(token)));
            orders = orderRepository.save(o);
        } catch (DataIntegrityViolationException ex) {
            throw new OrderException(ErrorCode.CONFLICT, "Meeting 시간 중복입니다.");
        }

        if (files != null) {
            files.stream()
                    .map(f -> uploadService.upload(f, "references"))
                    .map(url -> saveFile(orders, url))
                    .collect(Collectors.toList());
        }

        OrderDetail detail = OrderDetail.builder()
                .id(orders.getId())
                .state(CHECK)
                .orders(orders)
                .likes(0)
                .userId(orders.getUser().getId())
                .build();

        detailRepository.save(detail);
    }

    public void checkOrder(String token, Long orderId) {
        if (!checkAdmin(token)) throw new AdminException(ErrorCode.UNAUTHORIZED, "회원은 수정 불가능합니다.");

        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.NOT_FOUND, "Order is not exist."));
        detail.updateState(START);
    }

    @Transactional
    public void updateOrderDetail(String token, Long orderId,
                                  List<MultipartFile> images, OrderDetailRequestDto requestDto) {
        if (!checkAdmin(token)) throw new AdminException(ErrorCode.UNAUTHORIZED, "회원은 수정 불가능합니다.");

        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.NOT_FOUND, "Order is not exist."));
        if (detail.getState() == COMPLETED) throw new OrderException(ErrorCode.BAD_REQUEST, "이미 완료된 발주입니다.");

        OrderState state = findWithKey(requestDto.getStateKey());
        if (images != null) updateImage(detail, images);
        if (state != null) detail.updateState(state);
        detail.update(requestDto);
    }

    @Transactional
    public void addRating(String token, Long orderId, OrderRatingDto rating) {
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        OrderDetail detail = detailRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.BAD_REQUEST, "Order is not exist"));

        if (detail.getState() != COMPLETED) throw new OrderException(ErrorCode.BAD_REQUEST, "완료된 발주가 아닙니다.");
        if (detail.getUserId() != user.getId()) throw new OrderException(ErrorCode.BAD_REQUEST,
                "발주자만 별점 부여 가능합니다");
        if (detail.getRating() != null) throw new OrderException(ErrorCode.BAD_REQUEST, "이미 별점이 부여되었습니다.");
        detail.setRating(rating.getRating());
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
                .map(i -> uploadService.upload(i, "images"))
                .map(url -> saveImage(detail, url))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeOrder(String token, Long orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.BAD_REQUEST, "Order is not exist"));
        if (checkAdmin(token)) {
            orderRepository.delete(orders);
            return;
        }
        User findUser = userRepository.findByEmail(jwtProvider.getUid(token));
        if (orders.getUser() != findUser) throw new OrderException(ErrorCode.BAD_REQUEST, "발주자만 취소 가능합니다");
        if (detailRepository.findStateById(orderId) != CHECK)
            throw new OrderException(ErrorCode.BAD_REQUEST, "작업을 시작한 발주는 취소할 수 없습니다. 별도로 문의해주세요");
        orderRepository.delete(orders);
    }

    @Transactional
    public Boolean checkSiteNameDuplicate(String siteName) {
        Orders orders = orderRepository.findBySiteName(siteName);
        if (orders == null) {
            return false;
        }
        return true;
    }

    private OrderFile saveFile(Orders orders, String url) {
        OrderFile orderFile = OrderFile.builder()
                .s3Url(url)
                .fileName(StringUtils.getFilename(url))
                .orders(orders)
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

    private boolean checkAdmin(String token) {
        String role = jwtProvider.parseClaims(token).get("role").toString();
        return role.equals(ADMIN.toString());
    }
}
