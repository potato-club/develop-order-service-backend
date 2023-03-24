package com.server.dos.controller;

import com.server.dos.dto.OrderDetailListDto;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final OrderService orderService;

    @Operation(summary = "유저의 발주 디테일 리스트 반환")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetailListDto>> getOrderDetailListByUser(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderDetailListDto> orderDetailListByToken = orderService.getOrderDetailListByToken(token, page);
        return ResponseEntity.ok(orderDetailListByToken);
    }
}
