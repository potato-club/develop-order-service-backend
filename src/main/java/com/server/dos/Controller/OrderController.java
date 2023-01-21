package com.server.dos.Controller;

import com.server.dos.Dto.OrderRequestDto;
import com.server.dos.Dto.OrderResponseDto;
import com.server.dos.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    public List<OrderResponseDto> getAllOrder() {
        return orderService.getAllOrder();
    }

    @PostMapping("")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderDto) {
        orderService.createOrder(orderDto);
        return ResponseEntity.ok("발주 성공");
    }
}
