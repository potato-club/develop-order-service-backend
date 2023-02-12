package com.server.dos.mapper;

import com.server.dos.dto.OrderResponseDto;
import com.server.dos.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDto toResponse(Order order);
}
