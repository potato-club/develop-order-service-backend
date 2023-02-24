package com.server.dos.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.dos.Entity.Order;
import com.server.dos.Enum.OrderState;
import com.server.dos.Enum.SiteOwner;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Data
public class OrderRequestDto {
    private String siteName;
    private String purpose;
    private SiteOwner owner;
    private List<String> brandColor;
    private int page;
    private Boolean login;
    private Boolean database;
    private int duration;
    private String meeting;

    public Order toEntity() {
        return Order.builder()
                .siteName(siteName)
                .purpose(purpose)
                .owner(owner)
                .state(OrderState.WORKING)
                .brandColor(brandColor)
                .database(database)
                .duration(duration)
                .login(login)
                .meeting(parseDate(meeting))
                .page(page)
                .build();
    }

    public LocalDateTime parseDate(String meeting) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(meeting, formatter);
    }
}
