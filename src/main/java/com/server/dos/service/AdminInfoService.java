package com.server.dos.service;

import com.server.dos.dto.AdminInfoRequestDto;
import com.server.dos.repository.AdminInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminInfoService {

    private final AdminInfoRepository adminInfoRepository;

    @Transactional
    public void saveInfo(AdminInfoRequestDto infoRequestDto){
        adminInfoRepository.save(infoRequestDto.toEntity());
    }
}
