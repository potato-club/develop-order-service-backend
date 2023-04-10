package com.server.dos.dto;

import com.server.dos.entity.AdminInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class AdminInfoRequestDto {

    @ApiModelProperty(value = "직원 이름", example = "박해연", required = true)
    private String name;
    @ApiModelProperty(value = "직원 이메일", example = "hi123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "직원의 기술", example = "back", required = true)
    private String tech;
    @ApiModelProperty(value = "직원 전화번호", example = "010-XXXX-XXXX", required = true)
    private String phone;

    @ApiModelProperty(value = "직원 스케줄 시작일", example = "2023-03-20T08:00", required = true)
    private String start;
    @ApiModelProperty(value = "직원 스케줄 종료일", example = "2023-05-20T12:00", required = true)
    private String end;
    @ApiModelProperty(value = "스케줄 제목", example = "이것저것 합니당!", required = true)
    private String title;
    @ApiModelProperty(value = "직원별 색상", example = "#00aabb", required = true)
    private String color;

    public AdminInfo toEntity(){
        return AdminInfo.builder()
                .name(name)
                .email(email)
                .tech(tech)
                .phone(phone)
                .start(LocalDateTime.parse(start))
                .end(LocalDateTime.parse(end))
                .title(title)
                .color(color)
                .build();
    }
}
