package com.server.dos.dto;

import com.server.dos.entity.AdminInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminInfoRequestDto {
    private String name;
    private String email;
    private String tech;
    private String phone;
    private String title;
    private String color;

    public AdminInfo toEntity(){
        return AdminInfo.builder()
                .name(name)
                .email(email)
                .tech(tech)
                .phone(phone)
                .title(title)
                .color(color)
                .build();
    }
}
