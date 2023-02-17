package com.server.dos.mapper;

import com.server.dos.dto.FileDto;
import com.server.dos.entity.OrderFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileDto toDto(OrderFile file);
}
