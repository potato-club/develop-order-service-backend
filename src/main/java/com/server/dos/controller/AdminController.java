package com.server.dos.controller;

import com.server.dos.dto.*;
import com.server.dos.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "관리자 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
@RestController
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSaveRequestDto saveRequestDto){
        return ResponseEntity.ok(adminService.joinAdmin(saveRequestDto));
    }
    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> adminLogin(@RequestBody AdminLoginDto adminLoginDto){
        TokenDto adminToken = adminService.loginAdmin(adminLoginDto);
        return ResponseEntity.ok(adminToken);
    }

    // == 미완성 ==
    @Operation(summary = "직원 정보 등록")
    @PostMapping ("/info/register")
    public ResponseEntity<String> findAdminInfo(@RequestBody AdminInfoRequestDto adminInfoRequestDto){
        adminService.saveInfo(adminInfoRequestDto);
        return ResponseEntity.ok("직원 등록 완료");
    }

    // == 미완성 ==
    @Operation(summary = "직원 정보 수정")
    @PutMapping("/info/{adminId}/update")
    public ResponseEntity<String> updateAdminInfo(@PathVariable("adminId")Long id, @RequestBody AdminInfoUpdateDto updateDto){
        adminService.updateInfo(id,updateDto);
        return ResponseEntity.ok("직원정보 수정완료");
    }

    // == 미완성 ==
    @Operation(summary = "직원 정보 삭제")
    @DeleteMapping("/info/{adminId}/delete")
    public ResponseEntity deleteAdminInfo(@PathVariable("adminId")Long id){
        adminService.deleteInfo(id);
        return ResponseEntity.ok("직원정보 삭제완료");
    }

    // === 미완성 ====
    @Operation(summary = "직원 정보 반환")
    @GetMapping("/info")
    public ResponseEntity<List<AdminListResponseDto>> getAdminInfo(){
        List<AdminListResponseDto> adminInfo = adminService.getAdminInfo();
        return ResponseEntity.ok(adminInfo);
    }

    // === 미완성 ====
    @Operation(summary = "직원 스케줄 반환")
    @GetMapping("/{adminName}/schedule")
    public ResponseEntity<AdminScheduleDto> getAdminSchedule(@PathVariable("adminName")String adminName){
        log.info("PathVariable을 통한 관리자이름: " + adminName);
        AdminScheduleDto adminSchedule = adminService.getAdminSchedule(adminName);
        return ResponseEntity.ok(adminSchedule);
    }
}
