package com.server.dos.mapper;

import com.server.dos.dto.OrderDetailDto;
import com.server.dos.dto.OrderDetailListDto;
import com.server.dos.dto.OrderResponseDto;
import com.server.dos.entity.Order;
import com.server.dos.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ImageMapper.class, FileMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDto toResponse(Order order);

    @Mapping(source = "order.siteName", target = "siteName")
    @Mapping(source = "order.purpose", target = "purpose")
    @Mapping(source = "order.createdDate", target = "createdDate")
    @Mapping(source = "order.database", target = "database")
    @Mapping(source = "order.login", target = "login")
    @Mapping(source = "order.page", target = "page")
    OrderDetailDto toDetailDto(OrderDetail detail);

    @Mapping(source = "order.siteName", target = "siteName")
    @Mapping(source = "order.purpose", target = "purpose")
    @Mapping(source = "order.createdDate", target = "createdDate")
    OrderDetailListDto toDetailListDto(OrderDetail detail);
}
