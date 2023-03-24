package com.server.dos.dto;

import com.server.dos.entity.user.Admin;
import com.server.dos.entity.user.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminResponseDto {

//    private String adminId;
    private String adminName;
    private String adminEmail;
    private String adminPhone;
    private String adminTech;

    private Role role;

    public AdminResponseDto(Admin entity){
//        this.adminId = entity.getAdminId();
        this.adminName = entity.getAdminName();
        this.adminEmail = entity.getAdminEmail();
        this.adminPhone = entity.getAdminPhone();
        this.adminTech = entity.getAdminTech();
        this.role = entity.getRole();
    }
}
