package com.server.dos.entity;

import com.server.dos.Enum.OrderState;
import com.server.dos.dto.OrderDetailRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.server.dos.Enum.OrderState.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "orders_details")
public class OrderDetail extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "orderDetail", orphanRemoval = true)
    private List<OrderImage> images = new ArrayList<>();
    private Double rating;
    private LocalDateTime completedDate;
    private int likes;
    private Long userId;

    @OneToOne
    @JoinColumn(name = "orders_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    public void update(OrderDetailRequestDto requestDto) {
        this.order.update(requestDto);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void updateState(OrderState state) {
        this.state = state;
        if(state == COMPLETED) this.completedDate = LocalDateTime.now();
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
