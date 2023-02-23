package com.server.dos.service;

import com.server.dos.dto.*;
import com.server.dos.entity.OrderImage;
import com.server.dos.entity.Order;
import com.server.dos.entity.OrderDetail;
import com.server.dos.entity.OrderFile;
import com.server.dos.exception.custom.OrderException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.mapper.ImageMapper;
import com.server.dos.repository.OrderDetailRepository;
import com.server.dos.repository.OrderFileRepository;
import com.server.dos.repository.OrderImageRepository;
import com.server.dos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.Enum.OrderState.*;
import static com.server.dos.mapper.OrderMapper.INSTANCE;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderFileRepository fileRepository;
    private final OrderDetailRepository detailRepository;
    private final OrderImageRepository imageRepository;
    private final S3Service uploadService;

    @Transactional
    public List<OrderResponseDto> getAllOrder() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(INSTANCE::toResponse).collect(Collectors.toList());
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
        List<OrderDetail> details;
        if (state.equals("complete")) {
            details = detailRepository.findOrderDetailsByStateOrderByIdDesc(COMPLETE);
        } else {
            details = detailRepository.findOrderDetailsByStateOrderByIdDesc(WORKING);
        }
        List<OrderDetailListDto> detailListDtos = details.stream().map(INSTANCE::toDetailListDto)
                .map(dto -> addThumbnail(dto)).collect(Collectors.toList());

        return listPaging(page, detailListDtos);
    }

    // 메인 페이지 OrderList 정보 가져오기 (완료된 발주 중 좋아요 높은 순 3개)
    @Transactional
    public List<OrderMainDto> getMainOrders() {
        return null;
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
    public void createOrder(List<MultipartFile> files, OrderRequestDto orderDto) {
        Order order = orderRepository.save(orderDto.toEntity());

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

    private Page<OrderDetailListDto> listPaging(int page, List<OrderDetailListDto> detailListDtos) {
        Pageable pageable = PageRequest.of(page - 1, 4);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), detailListDtos.size());

        return new PageImpl<>(detailListDtos.subList(start, end), pageable, detailListDtos.size());
    }
}
