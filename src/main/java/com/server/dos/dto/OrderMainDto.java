package com.server.dos.dto;

import lombok.Data;

@Data
public class OrderMainDto {
    private Long id;
    private String siteName;
    private ImageDto thumbnail;
    private Double rating;
    private int likes;

    public OrderMainDto(OrderDetailListDto dto) {
        this.id = dto.getId();
        this.siteName = dto.getSiteName();
        this.thumbnail = dto.getThumbnail();
        this.rating = dto.getRating();
        this.likes = dto.getLikes();
    }
}
