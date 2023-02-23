package com.server.dos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailRequestDto {
    private LocalDateTime completedDate;
    private Double rating;
    private Boolean database;
    private Boolean login;
    private Integer page;
}
