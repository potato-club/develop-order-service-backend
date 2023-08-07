package com.server.dos.repository;

import com.server.dos.Enum.OrderState;
import com.server.dos.entity.Orders;
import com.server.dos.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findBySiteName(String siteName);
    List<Orders> findByUser(User user);

    @Query("SELECT o FROM Orders o JOIN o.detail od WHERE od.state = :state")
    Page<Orders> findByOrderDetailState(Pageable pageable, OrderState state);

    @Query("SELECT o FROM Orders o JOIN o.detail od WHERE od.state != com.server.dos.Enum.OrderState.CHECK AND " +
            "od.state != com.server.dos.Enum.OrderState.COMPLETED")
    Page<Orders> findByOrderDetailWorking(Pageable pageable);
}
