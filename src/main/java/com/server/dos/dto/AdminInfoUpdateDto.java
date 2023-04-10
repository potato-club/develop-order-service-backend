package com.server.dos.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminInfoUpdateDto {
    private String name;
    private String email;
    private String tech;
    private String phone;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String color;
}
