package com.server.dos.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50,unique = true)
    private String adminId;

    @Column(nullable = false,length = 100)
    private String adminPw;

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false,unique = true)
    private String adminEmail;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Admin(String adminId,String adminPw,String adminName,String adminEmail,Role role){
        this.adminId = adminId;
        this.adminPw = adminPw;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.role = role;
    }

}
