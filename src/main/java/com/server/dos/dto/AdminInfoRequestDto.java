package com.server.dos.dto;

import com.server.dos.entity.AdminInfo;
import com.server.dos.entity.user.Admin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class AdminInfoRequestDto {

    @ApiModelProperty(value = "직원 이름", example = "박해연", required = true)
    private String name;
    @ApiModelProperty(value = "직원 이메일", example = "hi123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "직원의 기술", example = "front,back,etc..", required = true)
    private String tech;
    @ApiModelProperty(value = "직원 전화번호", example = "010-XXXX-XXXX", required = true)
    private String phone;


    public AdminInfo toEntity(Admin admin){
        return AdminInfo.builder()
                .name(name)
                .email(email)
                .tech(tech)
                .phone(phone)
                .admin(admin)
                .build();
    }
}
