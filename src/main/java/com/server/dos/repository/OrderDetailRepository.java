package com.server.dos.repository;

import com.server.dos.Enum.OrderState;
import com.server.dos.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findOrderDetailsByStateOrderByIdDesc(OrderState state, Pageable pageable);
    List<OrderDetail> findTop5ByOrderByLikesDesc();
}
