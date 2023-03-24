package com.server.dos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminLoginDto {
    private  String adminId;
    private  String adminPw;

    public AdminLoginDto(String adminId,String adminPw){
        this.adminId = adminId;
        this.adminPw = adminPw;
    }
}
