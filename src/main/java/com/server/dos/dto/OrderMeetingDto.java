package com.server.dos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderMeetingDto {
    private String name;
    private LocalDateTime meeting;
}
