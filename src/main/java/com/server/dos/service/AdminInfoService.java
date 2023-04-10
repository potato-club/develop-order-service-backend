package com.server.dos.service;

import com.server.dos.dto.AdminInfoRequestDto;
import com.server.dos.dto.AdminInfoUpdateDto;
import com.server.dos.dto.AdminListResponseDto;
import com.server.dos.dto.AdminScheduleDto;
import com.server.dos.entity.AdminInfo;
import com.server.dos.repository.AdminInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.mapper.AdminMapper.INSTANCE;

@RequiredArgsConstructor
@Service
public class AdminInfoService {

    private final AdminInfoRepository adminInfoRepository;

    @Transactional
    public void saveInfo(AdminInfoRequestDto infoRequestDto){
        adminInfoRepository.save(infoRequestDto.toEntity());
    }

    // 직원 정보 조회
    @Transactional(readOnly = true)
    public List<AdminListResponseDto> getAdminInfo(){
        List<AdminInfo> allAdmin = adminInfoRepository.findAll();
        return allAdmin.stream()
                .map(INSTANCE::toInfoResponse)
                .collect(Collectors.toList());
    }

    // 직원 스케줄 조회
    @Transactional(readOnly = true)
    public AdminScheduleDto getAdminSchedule(String adminName){
        AdminInfo adminInfo = adminInfoRepository.findByName(adminName);
        if(adminInfo == null){throw new IllegalArgumentException("해당 관리자가 존재하지 않습니다.");}
        return INSTANCE.toScheduleResponse(adminInfo);
    }

    // 직원 정보 수정
    @Transactional
    public void updateInfo(Long adminId, AdminInfoUpdateDto updateDto){
        AdminInfo info = adminInfoRepository.findById(adminId).orElseThrow(()->new IllegalArgumentException("존재하지 않은 직원입니다."));
        info.update(updateDto);
    }

    // 직원 정보 삭제
    @Transactional
    public void deleteInfo(Long id){
        AdminInfo info = adminInfoRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않은 직원입니다."));
        adminInfoRepository.delete(info);
    }
}
