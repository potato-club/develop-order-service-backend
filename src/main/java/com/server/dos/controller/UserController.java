package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.OrderDetailListDto;
import com.server.dos.dto.UserDto;
import com.server.dos.entity.user.User;
import com.server.dos.repository.UserRepository;
import com.server.dos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Operation(summary = "유저의 발주 디테일 리스트 반환")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDetailListDto>> getOrderDetailListByUser(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        Page<OrderDetailListDto> orderDetailListByToken = orderService.getOrderDetailListByToken(token, page);
        return ResponseEntity.ok(orderDetailListByToken);
    }

    @Operation(summary = "유저 정보 전달")
    @GetMapping("")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader(value = "Authorization")String token){
        User user = userRepository.findByEmail(jwtProvider.getUid(token));
        UserDto userDto = UserDto.builder().email(user.getEmail()).name(user.getName()).picture(user.getPicture()).build();
        return ResponseEntity.ok(userDto);
    }
}
