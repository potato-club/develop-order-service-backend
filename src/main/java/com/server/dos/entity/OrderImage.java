package com.server.dos.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class OrderImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @Builder
    public OrderImage(String fileName, String s3Url, Order order) {
        this.fileName = fileName;
        this.s3Url = s3Url;
        this.order = order;
    }
}