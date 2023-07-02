package com.server.dos.repository;

import com.server.dos.Enum.OrderState;
import com.server.dos.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findOrderDetailsByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<OrderDetail> findTop5ByOrderByLikesDesc();

    @Query("select d from OrderDetail d where d.id=:id and d.state=com.server.dos.Enum.OrderState.COMPLETED")
    OrderDetail findCompleteById(Long id);

    @Query(value = "select d from OrderDetail d " +
            "where d.state = :state",
    countQuery = "select count(d) from OrderDetail d where d.state = :state")
    Page<OrderDetail> findCompletedDetails(Pageable pageable, OrderState state);

    @Query(value = "select d from OrderDetail d where not d.state = :state",
    countQuery = "select count(d) from OrderDetail d where not d.state = :state")
    Page<OrderDetail> findNotCompletedDetails(Pageable pageable, OrderState state);

    @Query("select d.state from OrderDetail d where d.id=:id")
    OrderState findStateById(Long id);
}
