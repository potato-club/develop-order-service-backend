package com.server.dos.entity;

import com.server.dos.dto.AdminInfoRequestDto;
import com.server.dos.entity.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "admin_info")
public class AdminInfo extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String tech;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;


    // 직원 정보 수정
    public void update(AdminInfoRequestDto updateDto){
        this.name = updateDto.getName();
        this.email = updateDto.getEmail();
        this.tech = updateDto.getTech();
        this.phone = updateDto.getPhone();
    }
}
