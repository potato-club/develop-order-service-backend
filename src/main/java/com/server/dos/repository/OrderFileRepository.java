package com.server.dos.repository;

import com.server.dos.entity.OrderFile;
import com.server.dos.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderFileRepository extends JpaRepository<OrderFile, Long> {
    List<OrderFile> findByOrders(Orders orders);
}
