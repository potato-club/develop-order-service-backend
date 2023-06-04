package com.server.dos.entity;

import com.server.dos.dto.AdminScheduleRequestDto;
import com.server.dos.entity.user.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "admin_schedule")
public class AdminSchedule extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public void update(AdminScheduleRequestDto requestDto){
        this.start = requestDto.getStart();
        this.end = requestDto.getEnd();
        this.title = requestDto.getTitle();
        this.color = requestDto.getColor();
    }
}
