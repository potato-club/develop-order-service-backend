package com.server.dos.Dto;

import com.server.dos.Entity.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    @ApiModelProperty(value = "고유 id", example = "1")
    private Long id;
    @ApiModelProperty(value = "사이트 이름", example = "DOS")
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "심심해서")
    private String purpose;
    @ApiModelProperty(value = "완성 희망 기간", example = "3")
    private int duration;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.siteName = order.getSiteName();
        this.purpose = order.getPurpose();
        this.duration = order.getDuration();
    }
}
