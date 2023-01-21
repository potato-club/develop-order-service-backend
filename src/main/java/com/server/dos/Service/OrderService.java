package com.server.dos.Service;

import com.server.dos.Dto.OrderRequestDto;
import com.server.dos.Dto.OrderResponseDto;
import com.server.dos.Entity.Order;
import com.server.dos.Enum.OrderState;
import com.server.dos.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public List<OrderResponseDto> getAllOrder() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void createOrder(OrderRequestDto orderDto) {
        Order order = orderDto.toEntity();
        orderRepository.save(order);
    }
}
