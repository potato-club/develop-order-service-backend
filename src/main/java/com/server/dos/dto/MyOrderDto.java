package com.server.dos.dto;

import com.server.dos.Enum.OrderState;
import lombok.Getter;

@Getter
public class MyOrderDto {
    private Long orderId;
    private OrderResponseDto orderDto;
    private OrderState state;
    public MyOrderDto(Long id, OrderResponseDto response, OrderState state) {
        this.orderId = id;
        this.orderDto = response;
        this.state = state;
    }
}
