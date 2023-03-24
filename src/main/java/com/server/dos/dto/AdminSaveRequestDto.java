package com.server.dos.dto;

import com.server.dos.entity.user.Admin;
import com.server.dos.entity.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor
@Getter
public class AdminSaveRequestDto {

    private String adminId;

    private String adminPw;

    private String adminName;

    private String adminEmail;

    private String adminTech;

    private String adminPhone;

    private Role role;

    @Builder
    public AdminSaveRequestDto(String adminId,String adminPw,String adminName,String adminEmail,String adminTech,String adminPhone,Role role){
        this.adminId = adminId;
        this.adminPw = adminPw;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminTech = adminTech;
        this.adminPhone = adminPhone;
        this.role = role;
    }
    public Admin toEntity(){
        return Admin.builder()
                .adminId(adminId)
                .adminPw(new BCryptPasswordEncoder().encode(adminPw))
                .adminName(adminName)
                .adminEmail(adminEmail)
                .adminTech(adminTech)
                .adminPhone(adminPhone)
                .role(Role.ADMIN)
                .build();
    }

}
