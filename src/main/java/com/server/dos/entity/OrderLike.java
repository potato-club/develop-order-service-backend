package com.server.dos.entity;

import com.server.dos.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders_likes")
public class OrderLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_detail_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Boolean status;

    public void unLikeOrder(OrderDetail detail) {
        detail.setLikes(detail.getLikes() - 1);
        this.status = false;
    }

    public OrderLike(OrderDetail orderDetail, User user) {
        this.orderDetail = orderDetail;
        this.user = user;
        this.status = true;
    }
}
