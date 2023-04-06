package com.server.dos.entity;

import com.server.dos.dto.AdminInfoUpdateDto;
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
public class AdminInfo {

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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    // 직원 정보 수정
    public void update(AdminInfoUpdateDto updateDto){
        this.name = updateDto.getName();
        this.email = updateDto.getEmail();
        this.tech = updateDto.getTech();
        this.phone = updateDto.getPhone();
        this.title = updateDto.getTitle();
        this.color = updateDto.getColor();
    }
}
