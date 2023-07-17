package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.AdminDto;
import com.server.dos.dto.OrderDetailListDto;
import com.server.dos.dto.UserDto;
import com.server.dos.entity.user.Admin;
import com.server.dos.entity.user.User;
import com.server.dos.exception.custom.UserException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.repository.AdminRepository;
import com.server.dos.repository.UserRepository;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String,Object> redisTemplate;

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

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@RequestHeader(value = "Authorization")String token){
        String email = jwtProvider.getUid(token);
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UserException(ErrorCode.BAD_REQUEST, "존재하지 않는 유저입니다.");
        userRepository.delete(user);
        String redis_token = (String) redisTemplate.opsForValue().get("User-RefreshToken-"+email);
        redisTemplate.delete(redis_token);
        return ResponseEntity.ok("회원탈퇴가 정상적으로 처리되었습니다.");
    }
}
