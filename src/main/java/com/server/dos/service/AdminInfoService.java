package com.server.dos.service;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.AdminInfoRequestDto;
import com.server.dos.dto.AdminInfoUpdateDto;
import com.server.dos.dto.AdminListResponseDto;
import com.server.dos.entity.AdminInfo;
import com.server.dos.repository.AdminInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.entity.user.Role.ADMIN;
import static com.server.dos.mapper.AdminMapper.INSTANCE;

@RequiredArgsConstructor
@Service
public class AdminInfoService {

    private final AdminInfoRepository adminInfoRepository;
    private final JwtProvider jwtProvider;

    // 직원 정보 등록
    @Transactional
    public void saveInfo(String token,AdminInfoRequestDto infoRequestDto){
        boolean check = checkRole(token);
        if (!check){
            throw new IllegalArgumentException("관리자가 아니면 삭제가 불가능합니다.");
        }
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

    // 직원 정보 수정
    @Transactional
    public void updateInfo(String token,Long adminId, AdminInfoUpdateDto updateDto){
        boolean check = checkRole(token);
        if (!check){
            throw new IllegalArgumentException("관리자가 아니면 수정이 불가능합니다.");
        }
        AdminInfo info = adminInfoRepository.findById(adminId).orElseThrow(()->new IllegalArgumentException("존재하지 않은 직원입니다."));
        info.update(updateDto);
    }

    // 직원 정보 삭제
    @Transactional
    public void deleteInfo(String token,Long id){
        boolean check = checkRole(token);
        if (!check){
            throw new IllegalArgumentException("관리자가 아니면 삭제가 불가능합니다.");
        }
        AdminInfo info = adminInfoRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않은 직원입니다."));
        adminInfoRepository.delete(info);
    }

    private boolean checkRole(String token){
        String role = jwtProvider.parseClaims(token).get("role").toString();
        return role.equals(ADMIN.toString());
    }
}
