package com.server.dos.repository;

import com.server.dos.entity.OrderDetail;
import com.server.dos.entity.OrderLike;
import com.server.dos.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLikeRepository extends JpaRepository<OrderLike, Long> {
    OrderLike findByOrderDetailAndUser(OrderDetail orderDetail, User user);
    @Query("SELECT o.orderDetail FROM OrderLike o WHERE o.user = :user ORDER BY o.orderDetail.createdDate DESC")
    Page<OrderDetail> findLikedOrderDetailsByUser(User user, Pageable pageable);
}
