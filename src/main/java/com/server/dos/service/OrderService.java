package com.server.dos.service;

import com.server.dos.dto.OrderRequestDto;
import com.server.dos.dto.OrderResponseDto;
import com.server.dos.entity.Order;
import com.server.dos.entity.OrderImage;
import com.server.dos.repository.OrderImageRepository;
import com.server.dos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.mapper.OrderMapper.INSTANCE;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderImageRepository imageRepository;
    private final S3UploadService uploadService;

    @Transactional
    public List<OrderResponseDto> getAllOrder() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(INSTANCE::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void createOrder(List<MultipartFile> images, OrderRequestDto orderDto) {
        Order order = orderDto.toEntity();
        orderRepository.save(order);

        images.stream()
                .map(uploadService::upload)
                .map(url -> saveImage(order, url))
                .collect(Collectors.toList());
    }

    private OrderImage saveImage(Order order, String url) {
        OrderImage orderImage = OrderImage.builder()
                .s3Url(url)
                .fileName(StringUtils.getFilename(url))
                .order(order)
                .build();
        return imageRepository.save(orderImage);
    }
}
