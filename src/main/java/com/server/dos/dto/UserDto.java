package com.server.dos.dto;

import com.server.dos.entity.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String picture;
    private Role role;
    @Builder
    public UserDto(String name,String email,String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = Role.USER;
    }
}