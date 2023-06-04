package com.server.dos.dto;

import com.server.dos.entity.AdminSchedule;
import com.server.dos.entity.user.Admin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminScheduleRequestDto {

    @ApiModelProperty(value = "직원 이름", example = "2023-03-20T08:00", required = true)
    private String name;
    @ApiModelProperty(value = "직원 스케줄 시작일", example = "2023-03-20T08:00", required = true)
    private LocalDateTime start;
    @ApiModelProperty(value = "직원 스케줄 종료일", example = "2023-05-20T12:00", required = true)
    private LocalDateTime end;
    @ApiModelProperty(value = "스케줄 제목", example = "이것저것 합니당!", required = true)
    private String title;
    @ApiModelProperty(value = "직원별 색상", example = "#00aabb", required = true)
    private String color;

    public AdminSchedule toEntity(Admin admin){
        return AdminSchedule.builder()
                .name(name)
                .start(start)
                .end(end)
                .title(title)
                .color(color)
                .admin(admin)
                .build();
    }
}
