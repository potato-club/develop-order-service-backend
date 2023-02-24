package com.server.dos.Dto;

import com.server.dos.Entity.Order;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long id;
    private String siteName;
    private String purpose;
    private int duration;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.siteName = order.getSiteName();
        this.purpose = order.getPurpose();
        this.duration = order.getDuration();
    }
}
