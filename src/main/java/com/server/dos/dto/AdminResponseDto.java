package com.server.dos.dto;

import com.server.dos.entity.AdminInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminResponseDto {

    private String name;
    private String email;
    private String tech;
    private String phone;
    private String title;
    private String color;

    public AdminResponseDto(AdminInfo entity){
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.tech = entity.getTech();
        this.phone = entity.getPhone();
        this.title = entity.getTitle();
        this.color = entity.getColor();
    }

}
