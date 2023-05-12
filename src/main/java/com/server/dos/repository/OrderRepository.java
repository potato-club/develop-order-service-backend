package com.server.dos.repository;

import com.server.dos.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findBySiteName(String siteName);
    Page<Orders> findAll(Pageable pageable);
}
