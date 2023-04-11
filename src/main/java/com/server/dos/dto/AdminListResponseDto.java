package com.server.dos.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdminListResponseDto {

    private String name;
    private String email;
    private String tech;
    private String phone;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String color;
}
