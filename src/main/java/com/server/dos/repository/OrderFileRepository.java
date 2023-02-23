package com.server.dos.repository;

import com.server.dos.entity.OrderFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFileRepository extends JpaRepository<OrderFile, Long> {
}
