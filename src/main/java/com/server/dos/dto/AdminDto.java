package com.server.dos.dto;

import com.server.dos.entity.user.Admin;
import com.server.dos.entity.user.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminDto {
    private String name;
    private String email;
    private String picture;
    private Role role;

    public AdminDto(Admin admin) {
        this.name = admin.getAdminName();
        this.email = admin.getAdminEmail();
        this.picture = "";
        this.role = Role.ADMIN;
    }
}
