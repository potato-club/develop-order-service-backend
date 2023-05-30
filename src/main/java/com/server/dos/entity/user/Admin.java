package com.server.dos.entity.user;

import lombok.*;

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
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public Admin(String adminId,String adminPw,Role role){
        this.adminId = adminId;
        this.adminPw = adminPw;
        this.role = role;
    }
}
