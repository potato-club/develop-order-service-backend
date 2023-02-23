package com.server.dos.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailDto {
    private Long id;
    private String siteName;
    private String purpose;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;
    private Double rating;
    private List<ImageDto> images;
    private Boolean database;
    private Boolean login;
    private Integer page;
}
