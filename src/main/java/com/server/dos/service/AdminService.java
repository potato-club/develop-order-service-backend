package com.server.dos.service;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.*;
import com.server.dos.entity.user.Admin;
import com.server.dos.exception.custom.AdminException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String,Object> redisTemplate;
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public TokenDto loginAdmin(AdminLoginDto adminLoginDto){

        Admin entity = adminRepository.findByAdminId(adminLoginDto.getAdminId());
        if(entity == null){
            throw new AdminException(ErrorCode.BAD_REQUEST,"해당 아이디는 관리자 아이디가 아닙니다.");
        }else if(!entity.getAdminPw().equals(adminLoginDto.getAdminPw())){
            throw new AdminException(ErrorCode.BAD_REQUEST,"관리자 비밀번호가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtProvider.generateToken(entity.getAdminId(), entity.getRole().toString());
        redisTemplate.opsForValue().set("Admin-RefreshToken",tokenDto.getRefreshToken());

        return tokenDto;
    }

}
