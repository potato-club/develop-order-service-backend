package com.server.dos.dto;

import com.server.dos.Enum.OrderState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailListDto {
    private Long id;
    private String siteName;
    private String purpose;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;
    private OrderState state;
    private Double rating;
    private int likes;
    private ImageDto thumbnail;
}
