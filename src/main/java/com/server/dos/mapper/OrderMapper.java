package com.server.dos.mapper;

import com.server.dos.dto.*;
import com.server.dos.entity.Orders;
import com.server.dos.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ImageMapper.class, FileMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "client.clientName", target = "name")
    @Mapping(source = "client.clientEmail", target = "email")
    @Mapping(source = "client.hotLine", target = "hotLine")
    @Mapping(source = "client.subLine", target = "subLine")
    OrderResponseDto toResponse(Orders orders);

    @Mapping(source = "client.clientName", target = "clientName")
    OrderListResponseDto toListResponse(Orders orders);

    @Mapping(source = "client.clientName", target = "name")
    OrderMeetingDto toMeeting(Orders orders);

    @Mapping(source = "orders.siteName", target = "siteName")
    @Mapping(source = "orders.purpose", target = "purpose")
    @Mapping(source = "orders.createdDate", target = "createdDate")
    @Mapping(source = "orders.needDatabase", target = "database")
    @Mapping(source = "orders.login", target = "login")
    @Mapping(source = "orders.page", target = "page")
    OrderDetailDto toDetailDto(OrderDetail detail);

    @Mapping(source = "orders.siteName", target = "siteName")
    @Mapping(source = "orders.purpose", target = "purpose")
    @Mapping(source = "orders.createdDate", target = "createdDate")
    @Mapping(source = "orders.client.clientName", target = "clientName")
    OrderDetailListDto toDetailListDto(OrderDetail detail);
}
