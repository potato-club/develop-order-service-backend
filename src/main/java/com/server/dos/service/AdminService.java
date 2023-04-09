package com.server.dos.service;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.*;
import com.server.dos.entity.AdminInfo;
import com.server.dos.entity.user.Admin;
import com.server.dos.exception.custom.AdminException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.repository.AdminInfoRepository;
import com.server.dos.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.mapper.AdminMapper.INSTANCE;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final AdminInfoRepository adminInfoRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Transactional
    public String joinAdmin(AdminSaveRequestDto saveRequestDto){
        boolean checkId = adminRepository.existsByAdminId(saveRequestDto.getAdminId());
//        boolean checkEmail = adminRepository.existsByAdminEmail(saveRequestDto.getAdminEmail());

        if(checkId){
            throw new AdminException(ErrorCode.INTERNAL_SERVER_ERROR,"이미 존재하는 아이디입니다.");}
//        }else if(checkEmail){
//            throw new AdminException(ErrorCode.INTERNAL_SERVER_ERROR,"이미 존재하는 이메일입니다.");
//        }
        adminRepository.save(saveRequestDto.toEntity());

        return "관리자 회원가입 완료";
    }

    @Transactional
    public TokenDto loginAdmin(AdminLoginDto adminLoginDto){
        Admin entity = adminRepository.findByAdminId(adminLoginDto.getAdminId());

        if(entity == null){
            throw new AdminException(ErrorCode.INTERNAL_SERVER_ERROR,"해당 아이디가 존재하지 않습니다.");
        }else if(!passwordEncoder.matches(adminLoginDto.getAdminPw(),entity.getAdminPw())){
            throw new AdminException(ErrorCode.INTERNAL_SERVER_ERROR,"비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.generateToken(entity.getAdminId(), "ADMIN");
    }

    // 직원 정보 조회
    @Transactional(readOnly = true)
    public List<AdminListResponseDto> getAdminInfo(){
        List<AdminInfo> allAdmin = adminInfoRepository.findAll();
        return allAdmin.stream()
                .map(INSTANCE::toInfoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminScheduleDto getAdminSchedule(String adminName){
        AdminInfo adminInfo = adminInfoRepository.findByName(adminName);
        if(adminInfo == null){throw new IllegalArgumentException("해당 관리자가 존재하지 않습니다.");}
        return INSTANCE.toScheduleResponse(adminInfo);
    }

    // 직원 정보 저장
    @Transactional
    public void saveInfo(AdminInfoRequestDto infoRequestDto){
        adminInfoRepository.save(infoRequestDto.toEntity());
    }

    // 직원 정보 수정
    @Transactional
    public void updateInfo(Long adminId,AdminInfoUpdateDto updateDto){
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
