package com.server.dos.dto;

import lombok.Data;

@Data
public class OrderMainDto {
    private Long id;
    private String siteName;
    private ImageDto thumbnail;

    public OrderMainDto(OrderDetailListDto dto) {
        this.id = dto.getId();
        this.siteName = dto.getSiteName();
        this.thumbnail = dto.getThumbnail();
    }
}
