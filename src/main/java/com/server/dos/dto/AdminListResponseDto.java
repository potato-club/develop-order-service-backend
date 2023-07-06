package com.server.dos.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class AdminListResponseDto {

    @ApiModelProperty(value = "직원 등록번호(고유 id값)")
    private Long id;
    @ApiModelProperty(value = "직원 이름", example = "박해연", required = true)
    private String name;
    @ApiModelProperty(value = "직원 이메일", example = "hi123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "직원 기술", example = "back", required = true)
    private String tech;
    @ApiModelProperty(value = "직원 번호", example = "010-1234-5678", required = true)
    private String phone;
}
