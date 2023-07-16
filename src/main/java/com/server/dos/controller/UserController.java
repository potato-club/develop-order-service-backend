package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.AdminDto;
import com.server.dos.dto.OrderDetailListDto;
import com.server.dos.dto.UserDto;
import com.server.dos.entity.user.Admin;
import com.server.dos.entity.user.User;
import com.server.dos.repository.AdminRepository;
import com.server.dos.repository.UserRepository;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.server.dos.entity.user.Role.ADMIN;

@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;

    @Operation(summary = "유저의 발주 디테일 리스트 반환")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetailListDto>> getOrderDetailListByUser(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderDetailListDto> orderDetailListByToken = orderService.getOrderDetailListByToken(token, page);
        return ResponseEntity.ok(orderDetailListByToken);
    }

    @Operation(summary = "유저의 좋아요 누른 발주 리스트 반환")
    @GetMapping("/orders/like")
    public ResponseEntity<Page<OrderDetailListDto>> getLikeOrderDetailListByUser(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderDetailListDto> likedOrderDetailList = orderService.getLikeOrderDetailListByToken(token, page);
        return ResponseEntity.ok(likedOrderDetailList);
    }

    @Operation(summary = "유저/관리자 정보 전달")
    @GetMapping("")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization")String token){
        String role = jwtProvider.parseClaims(token).get("role").toString();
        if(role.equals(ADMIN.toString())) {
            Admin admin = adminRepository.findByAdminLoginId(jwtProvider.getUid(token));
            AdminDto adminDto = new AdminDto(admin);
            return ResponseEntity.ok(adminDto);
        }
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        UserDto userDto = new UserDto(user);
        return ResponseEntity.ok(userDto);
    }
}
