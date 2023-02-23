package com.server.dos.mapper;

import com.server.dos.dto.ImageDto;
import com.server.dos.entity.OrderImage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    ImageDto toDto(OrderImage image);
}
