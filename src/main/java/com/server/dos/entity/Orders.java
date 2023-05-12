package com.server.dos.entity;

import com.server.dos.Enum.SiteOwner;
import com.server.dos.dto.OrderDetailRequestDto;
import com.server.dos.entity.user.User;
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
public class Orders extends BaseTimeEntity{
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
    private Boolean needDatabase;

    @OneToMany(mappedBy = "orders", orphanRemoval = true)
    private List<OrderFile> orderFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Column(nullable = false, unique = true)
    private LocalDateTime meeting;

    @Column
    private String etc;

    public void update(OrderDetailRequestDto requestDto) {
        if(requestDto.getDatabase() != null) this.needDatabase = requestDto.getDatabase();
        if(requestDto.getLogin() != null) this.login = requestDto.getLogin();
        if(requestDto.getPage() != null) this.page = requestDto.getPage();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
