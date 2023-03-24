package com.server.dos.controller;

import com.server.dos.dto.AdminLoginDto;
import com.server.dos.dto.AdminSaveRequestDto;
import com.server.dos.dto.TokenDto;
import com.server.dos.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@RequestBody AdminSaveRequestDto saveRequestDto){
        return ResponseEntity.ok(adminService.joinAdmin(saveRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> adminLogin(@RequestBody AdminLoginDto adminLoginDto){
        TokenDto adminToken = adminService.loginAdmin(adminLoginDto);
        return ResponseEntity.ok(adminToken);
    }
}
