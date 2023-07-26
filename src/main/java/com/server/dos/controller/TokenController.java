package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.TokenDto;
import com.server.dos.dto.UserDto;
import com.server.dos.exception.custom.TokenException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.service.TokenForTest;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Api(tags = "토큰 API")
@RequiredArgsConstructor
@RequestMapping("/token")
@Slf4j
@RestController
public class TokenController {

    private final JwtProvider jwtProvider;
    private final TokenForTest tokenForTest;

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAuth(HttpServletRequest request) throws IOException {
        String refreshToken = request.getHeader("refresh");

        try{
            if (refreshToken != null && jwtProvider.verifyToken(refreshToken)){
                String accessToken = jwtProvider.recreateAccessToken(refreshToken);
                TokenDto tokenDto = TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                return ResponseEntity.ok(tokenDto);
            }
        }catch (TokenException e){
            String msg = e.getErrorMessage();

            // 리프레시 토큰 만료 => 재로그인
            if (ErrorCode.EXPIRED_TOKEN.getMessage().equals(msg)){
                throw new TokenException(ErrorCode.RE_LOGIN,"모든 토큰이 만료되었습니다. 재로그인해주세요.");
            } else if (ErrorCode.INVALID_TOKEN.getMessage().equals(msg)) {
                throw new TokenException(ErrorCode.INVALID_TOKEN,"유효하지 않은 토큰 형식이나 구성으로 인해 토큰 검증에 실패했습니다.");
            } else if (ErrorCode.UNSUPPORTED_TOKEN.getMessage().equals(msg)) {
                throw new TokenException(ErrorCode.UNSUPPORTED_TOKEN,"예상하는 형식과 일치하지 않는 특정 형식이나 구성의 토큰입니다.");
            } else if (ErrorCode.MISSING_TOKEN.getMessage().equals(msg)) {
                throw new TokenException(ErrorCode.MISSING_TOKEN,"필요한 토큰이 누락되었습니다.");
            }
        }
        return ResponseEntity.ok("토큰 재발급에 실패하였습니다. 다시 진행해주세요");
    }


    @Operation(summary = "테스트를 위한 가입/토큰발급")
    @PostMapping("/dummy")
    public ResponseEntity<TokenDto> getUserTokenForTest(@RequestBody UserDto userDto) {
        TokenDto tokenDto = tokenForTest.addDummyUser(userDto);
        return ResponseEntity.ok(tokenDto);
    }
}
