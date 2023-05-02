package com.server.dos.repository;

import com.server.dos.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findBySiteName(String siteName);
    Page<Order> findAll(Pageable pageable);
}
