package com.server.dos.entity;

import com.server.dos.Enum.SiteOwner;
import com.server.dos.dto.OrderDetailRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "orders")
public class Order extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Embedded
    private Client client = new Client();

    @Column(nullable = false, unique = true)
    private String siteName;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SiteOwner owner;

    @Column
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> mainColor;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subColor;

    @Column
    private Integer page;

    @Column
    private Boolean login;

    @Column
    private Boolean database;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderFile> orderFiles = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime meeting;

    @Column
    private String etc;

    public void update(OrderDetailRequestDto requestDto) {
        if(requestDto.getDatabase() != null) this.database = requestDto.getDatabase();
        if(requestDto.getLogin() != null) this.login = requestDto.getLogin();
        if(requestDto.getPage() != null) this.page = requestDto.getPage();
    }
}
