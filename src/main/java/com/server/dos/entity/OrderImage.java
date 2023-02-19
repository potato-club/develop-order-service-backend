package com.server.dos.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderDetail orderDetail;

    @Builder
    public OrderImage(String imageName, String imageUrl, OrderDetail orderDetail) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.orderDetail = orderDetail;
    }
}
