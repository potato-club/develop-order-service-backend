package com.server.dos.controller;

import com.server.dos.dto.*;
import com.server.dos.service.AdminInfoService;
import com.server.dos.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "직원 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    private final AdminInfoService adminInfoService;

    @Operation(summary = "직원 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSaveRequestDto saveRequestDto){
        return ResponseEntity.ok(adminService.joinAdmin(saveRequestDto));
    }

    @Operation(summary = "직원 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> adminLogin(@RequestBody AdminLoginDto adminLoginDto){
        TokenDto adminToken = adminService.loginAdmin(adminLoginDto);
        return ResponseEntity.ok(adminToken);
    }

    @Operation(summary = "직원 정보 등록")
    @PostMapping("/info/register")
    public ResponseEntity<String> createAdminInfo(@RequestBody AdminInfoRequestDto infoRequestDto){
        adminInfoService.saveInfo(infoRequestDto);
        return ResponseEntity.ok("직원정보 등록완료");
    }

    @Operation(summary = "직원 정보 수정")
    @PutMapping("/info/{adminId}/update")
    public ResponseEntity<String> updateAdminInfo(@PathVariable("adminId")Long adminId, @RequestBody AdminInfoUpdateDto updateDto){
        adminInfoService.updateInfo(adminId,updateDto);
        return ResponseEntity.ok("직원정보 수정완료");
    }

    @Operation(summary = "직원 정보 삭제")
    @DeleteMapping("/info/{adminId}/delete")
    public ResponseEntity<String> deleteAdminInfo(@PathVariable("adminId")Long adminId){
        adminInfoService.deleteInfo(adminId);
        return ResponseEntity.ok("직원정보 삭제완료");
    }

    @Operation(summary = "직원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<?> getAdminInfo(){
        List<AdminListResponseDto> adminInfoList = adminInfoService.getAdminInfo();
        return ResponseEntity.ok(adminInfoList);
    }

    @Operation(summary = "직원 스케줄 조회")
    @GetMapping("/{adminName}/schedule")
    public ResponseEntity<?> getAdminSchedule(@PathVariable("adminName")String adminName){
        AdminScheduleDto adminSchedule = adminInfoService.getAdminSchedule(adminName);
        return ResponseEntity.ok(adminSchedule);
    }
}
