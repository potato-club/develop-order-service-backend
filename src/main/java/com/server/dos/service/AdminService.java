package com.server.dos.service;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.*;
import com.server.dos.entity.AdminInfo;
import com.server.dos.entity.AdminSchedule;
import com.server.dos.entity.user.Admin;
import com.server.dos.exception.custom.AdminException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.repository.AdminInfoRepository;
import com.server.dos.repository.AdminRepository;
import com.server.dos.repository.AdminScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.dos.entity.user.Role.ADMIN;
import static com.server.dos.mapper.AdminMapper.INSTANCE;


@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminInfoRepository adminInfoRepository;
    private final AdminScheduleRepository adminScheduleRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String,Object> redisTemplate;
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public TokenDto loginAdmin(AdminLoginDto adminLoginDto){

        Admin entity = adminRepository.findByAdminLoginId(adminLoginDto.getAdminId());
        if(entity == null){
            throw new AdminException(ErrorCode.BAD_REQUEST,"해당 아이디는 관리자 아이디가 아닙니다.");
        }else if(!entity.getAdminPw().equals(adminLoginDto.getAdminPw())){
            throw new AdminException(ErrorCode.BAD_REQUEST,"관리자 비밀번호가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtProvider.generateToken(entity.getAdminLoginId(), entity.getRole().toString());
        redisTemplate.opsForValue().set("Admin-RefreshToken",tokenDto.getRefreshToken());

        return tokenDto;
    }

    // 직원 정보 등록
    @Transactional
    public Long saveInfo(String token,AdminInfoRequestDto infoRequestDto){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 등록이 불가능합니다.");
        }
        Admin admin = adminRepository.findByAdminLoginId(jwtProvider.getUid(token));
        return adminInfoRepository.save(infoRequestDto.toEntity(admin)).getId();
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
    public void updateInfo(String token,Long adminId, AdminInfoRequestDto updateDto){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 수정이 불가능합니다.");
        }
        AdminInfo info = adminInfoRepository.findById(adminId)
                .orElseThrow(()->new AdminException(ErrorCode.BAD_REQUEST,"존재하지 않은 직원입니다."));
        info.update(updateDto);
    }

    // 직원 정보 삭제
    @Transactional
    public void deleteInfo(String token,Long id){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 삭제가 불가능합니다.");
        }
        AdminInfo info = adminInfoRepository.findById(id)
                .orElseThrow(()->new AdminException(ErrorCode.BAD_REQUEST,"존재하지 않은 직원입니다."));
        adminInfoRepository.delete(info);
    }

    // 직원 스케줄 저장
    @Transactional
    public Long createSchedule(String token,AdminScheduleRequestDto requestDto){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 삭제가 불가능합니다.");
        }
        Admin admin = adminRepository.findByAdminLoginId(jwtProvider.getUid(token));
        return adminScheduleRepository.save(requestDto.toEntity(admin)).getId();
    }

    // 직원 스케줄 조회(전체 회원 조회가능)
    @Transactional
    public List<AdminScheduleResponseDto> getSchedule(){
        List<AdminSchedule> allSche = adminScheduleRepository.findAll();
        return allSche.stream()
                .map(INSTANCE::toScheResponse)
                .collect(Collectors.toList());
    }

    // 직원 스케줄 수정
    @Transactional
    public void updateSchedule(String token,Long adminScheId,AdminScheduleRequestDto requestDto){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 삭제가 불가능합니다.");
        }
        AdminSchedule adminSchedule = adminScheduleRepository.findById(adminScheId)
                        .orElseThrow(()->new AdminException(ErrorCode.BAD_REQUEST,"존재하지 않은 직원스케줄입니다."));
        adminSchedule.update(requestDto);
    }

    // 직원 스케줄 삭제
    @Transactional
    public void deleteSchedule(String token, Long adminScheId){
        boolean check = checkRole(token);
        if (!check){
            throw new AdminException(ErrorCode.FORBIDDEN,"관리자가 아니면 삭제가 불가능합니다.");
        }
        AdminSchedule adminSchedule = adminScheduleRepository.findById(adminScheId)
                .orElseThrow(()->new AdminException(ErrorCode.BAD_REQUEST,"존재하지 않은 직원스케줄입니다."));
        adminScheduleRepository.delete(adminSchedule);
    }

    private boolean checkRole(String token) {
        String role = jwtProvider.parseClaims(token).get("role").toString();
        return role.equals(ADMIN.toString());
    }
}
