package com.server.dos.mapper;

import com.server.dos.dto.AdminResponseDto;
import com.server.dos.entity.AdminInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    AdminResponseDto toResponse(AdminInfo adminInfo);
}
