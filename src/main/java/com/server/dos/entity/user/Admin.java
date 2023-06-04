package com.server.dos.entity.user;

import com.server.dos.entity.AdminInfo;
import com.server.dos.entity.AdminSchedule;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(nullable = false,length = 50,unique = true)
    private String adminLoginId;

    @Column(nullable = false,length = 100)
    private String adminPw;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "admin")
    private List<AdminInfo> adminInfo = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    private List<AdminSchedule> adminSchedules = new ArrayList<>();

    public Admin(String adminId,String adminPw,Role role){
        this.adminLoginId = adminId;
        this.adminPw = adminPw;
        this.role = role;
    }
}
