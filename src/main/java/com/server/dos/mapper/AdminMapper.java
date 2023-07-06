package com.server.dos.mapper;

import com.server.dos.dto.AdminListResponseDto;
import com.server.dos.dto.AdminScheduleResponseDto;
import com.server.dos.entity.AdminInfo;
import com.server.dos.entity.AdminSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(source = "adminInfo.id",target = "id")
    @Mapping(source = "adminInfo.name", target = "name")
    @Mapping(source = "adminInfo.email",target = "email")
    @Mapping(source = "adminInfo.tech",target = "tech")
    @Mapping(source = "adminInfo.phone",target = "phone")
    AdminListResponseDto toInfoResponse(AdminInfo adminInfo);

    @Mapping(source = "schedule.id",target = "id")
    @Mapping(source = "schedule.name",target = "name")
    @Mapping(source = "schedule.start",target = "start")
    @Mapping(source = "schedule.end",target = "end")
    @Mapping(source = "schedule.title",target = "title")
    @Mapping(source = "schedule.color",target = "color")
    AdminScheduleResponseDto toScheResponse(AdminSchedule schedule);

}
