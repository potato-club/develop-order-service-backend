package com.server.dos.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminScheduleResponseDto {

    @ApiModelProperty(value = "직원 등록번호(고유 id값)")
    private Long id;
    @ApiModelProperty(value = "직원 이름",example = "haeyeon")
    private String name;
    @ApiModelProperty(value = "직원 스케줄 시작일", example = "2023-03-20T08:00", required = true)
    private String start;
    @ApiModelProperty(value = "직원 스케줄 종료일", example = "2023-05-20T12:00", required = true)
    private String end;
    @ApiModelProperty(value = "스케줄 제목", example = "이것저것 합니당!", required = true)
    private String title;
    @ApiModelProperty(value = "직원별 색상", example = "#00aabb", required = true)
    private String color;
}