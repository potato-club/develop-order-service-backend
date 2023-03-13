package com.server.dos.repository;

import com.server.dos.entity.OrderDetail;
import com.server.dos.entity.OrderLike;
import com.server.dos.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLikeRepository extends JpaRepository<OrderLike, Long> {
    OrderLike findByOrderDetailAndUser(OrderDetail orderDetail, User user);
}
