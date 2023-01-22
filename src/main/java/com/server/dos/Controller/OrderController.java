package com.server.dos.Controller;

import com.server.dos.Dto.OrderRequestDto;
import com.server.dos.Dto.OrderResponseDto;
import com.server.dos.Service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderDto) {
        orderService.createOrder(orderDto);
        return ResponseEntity.ok("발주 성공");
    }
}
