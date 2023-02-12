package com.server.dos.controller;

import com.server.dos.dto.OrderRequestDto;
import com.server.dos.dto.OrderResponseDto;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "발주 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "모든 발주 리스트 반환")
    @GetMapping("")
    public List<OrderResponseDto> getAllOrder() {
        return orderService.getAllOrder();
    }

    @Operation(summary = "발주 신청")
    @PostMapping("")
    public ResponseEntity<String> createOrder(@RequestPart(value = "images") List<MultipartFile> images,
                                              @RequestPart(value = "orderDto") OrderRequestDto orderDto) {
        orderService.createOrder(images, orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("발주 성공");
    }
}
