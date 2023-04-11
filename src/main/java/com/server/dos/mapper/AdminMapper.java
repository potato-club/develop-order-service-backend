package com.server.dos.mapper;

import com.server.dos.dto.AdminListResponseDto;
import com.server.dos.entity.AdminInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(source = "adminInfo.name", target = "name")
    @Mapping(source = "adminInfo.email",target = "email")
    @Mapping(source = "adminInfo.tech",target = "tech")
    @Mapping(source = "adminInfo.phone",target = "phone")
    @Mapping(source = "adminInfo.start",target = "start")
    @Mapping(source = "adminInfo.end",target = "end")
    @Mapping(source = "adminInfo.title",target = "title")
    @Mapping(source = "adminInfo.color",target = "color")
    AdminListResponseDto toInfoResponse(AdminInfo adminInfo);

}
