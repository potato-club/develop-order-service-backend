package com.server.dos.controller;

import com.server.dos.dto.*;
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

    @Operation(summary = "직원 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> adminLogin(@RequestBody AdminLoginDto adminLoginDto){
        TokenDto adminToken = adminService.loginAdmin(adminLoginDto);
        return ResponseEntity.ok(adminToken);
    }

    @Operation(summary = "직원 정보 등록")
    @PostMapping("/info/register")
    public ResponseEntity<Long> createAdminInfo(@RequestHeader("Authorization")String token,
                                                  @RequestBody AdminInfoRequestDto infoRequestDto){
        return ResponseEntity.ok(adminService.saveInfo(token,infoRequestDto));
    }

    @Operation(summary = "직원 정보 수정")
    @PutMapping("/info")
    public ResponseEntity<String> updateAdminInfo(@RequestHeader("Authorization")String token,
                                                  @RequestParam("AdminInfoId")Long adminId,
                                                  @RequestBody AdminInfoRequestDto updateDto){
        adminService.updateInfo(token,adminId,updateDto);
        return ResponseEntity.ok("직원정보 수정완료");
    }

    @Operation(summary = "직원 정보 삭제")
    @DeleteMapping("/info")
    public ResponseEntity<String> deleteAdminInfo(@RequestHeader("Authorization")String token,
                                                  @RequestParam("AdminInfoId") Long adminId){
        adminService.deleteInfo(token,adminId);
        return ResponseEntity.ok("직원정보 삭제완료");
    }

    @Operation(summary = "직원 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<?> getAdminInfo(){
        List<AdminListResponseDto> adminInfoList = adminService.getAdminInfo();
        return ResponseEntity.ok(adminInfoList);
    }

    @Operation(summary = "직원 스케줄 저장")
    @PostMapping("/schedule/register")
    public ResponseEntity<Long> createSchedule(@RequestHeader("Authorization")String token,
                                                 @RequestBody AdminScheduleRequestDto requestDto){
        return ResponseEntity.ok(adminService.createSchedule(token,requestDto));
    }

    @Operation(summary = "직원 스케줄 수정")
    @PutMapping("/schedule")
    public ResponseEntity<String> updateSchedule(@RequestHeader("Authorization")String token,
                               @RequestParam("AdminScheduleId") Long adminScheduleId,
                               @RequestBody AdminScheduleRequestDto requestDto){
        adminService.updateSchedule(token,adminScheduleId,requestDto);
        return ResponseEntity.ok("직원 스케줄이 수정되었습니다");
    }

    @Operation(summary = "직원 스케줄 조회")
    @GetMapping("/schedule")
    public ResponseEntity<List<AdminScheduleResponseDto>> getSchedule(){
        List<AdminScheduleResponseDto> schedule = adminService.getSchedule();
        return ResponseEntity.ok(schedule);
    }

    @Operation(summary = "직원 스케줄 삭제")
    @DeleteMapping("/schedule")
    public ResponseEntity<String> deleteSchedule(@RequestHeader("Authorization")String token,
            @RequestParam("AdminScheduleId") Long AdminScheduleId){
        adminService.deleteSchedule(token,AdminScheduleId);
        return ResponseEntity.ok("직원 스케줄 삭제가 완료되었습니다.");
    }
}
