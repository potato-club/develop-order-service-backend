
package com.server.dos.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderListResponseDto {
    @ApiModelProperty(value = "고유 id", example = "1")
    private Long id;
    @ApiModelProperty(value = "사이트 이름", example = "DOS")
    private String siteName;
    @ApiModelProperty(value = "사이트 목적", example = "심심해서")
    private String purpose;
    @ApiModelProperty(value = "발주 신청자", example = "JOE")
    private String clientName;
    @ApiModelProperty(value = "발주 신청날")
    private LocalDateTime createdDate;

}