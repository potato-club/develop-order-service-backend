package com.server.dos.Entity;

import com.server.dos.Enum.OrderState;
import com.server.dos.Enum.SiteOwner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "Orders")
public class Order extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String siteName;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SiteOwner owner;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> brandColor;

    @Column(nullable = false)
    private int page;

    @Column(nullable = false)
    private Boolean login;

    @Column(nullable = false)
    private Boolean database;

    @Column
    private int duration;

    @Column(nullable = false)
    private LocalDateTime meeting;

    @Column(nullable = false)
    private OrderState state;
}
