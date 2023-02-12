package com.server.dos.repository;

import com.server.dos.entity.OrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderImageRepository extends JpaRepository<OrderImage, Long> {
}
