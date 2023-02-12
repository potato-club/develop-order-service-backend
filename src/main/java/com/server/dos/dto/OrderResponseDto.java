package com.server.dos.dto;

import com.server.dos.Enum.OrderState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderResponseDto {
    @ApiModelProperty(value = "고유 id", example = "1")
    private Long id;
    @ApiModelProperty(value = "사이트 이름", example = "DOS")
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "심심해서")
    private String purpose;
    @ApiModelProperty(value = "발주 상태", example = "WORKING")
    private OrderState state;

    // 이미지 추가
}