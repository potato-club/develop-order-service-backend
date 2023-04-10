package com.server.dos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminScheduleDto {
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String color;
}
