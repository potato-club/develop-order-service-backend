package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.TokenDto;
import com.server.dos.dto.UserDto;
import com.server.dos.exception.custom.TokenException;
import com.server.dos.exception.error.ErrorCode;
import com.server.dos.service.TokenForTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final JwtProvider jwtProvider;
    private final TokenForTest tokenForTest;

    @GetMapping("/token/expired")
    public String auth(){
        throw new TokenException(ErrorCode.UNAUTHORIZED,"토큰이 만료되었습니다.");
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("refresh");

        if (token != null && jwtProvider.verifyToken(token)) {
            String email = jwtProvider.getUid(token);
            TokenDto newToken = jwtProvider.generateToken(email, "USER");

            response.addHeader("Auth", newToken.getAccessToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");


//            String tagUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/login/")
//                    .queryParam("accesstoken",newToken.getAccessToken())
//                    .queryParam("refresh",newToken.getRefreshToken())
//                    .build().toUriString();
//
//
//            getRedirectStrategy().sendRedirect(request,response,tagUrl);
            return "NEW TOKEN";
        }

        throw new TokenException(ErrorCode.UNAUTHORIZED,"토큰 재발급에 실패하였습니다.");
    }

    @PostMapping("/token/dummy")
    public ResponseEntity<TokenDto> getUserTokenForTest(@RequestBody UserDto userDto) {
        TokenDto tokenDto = tokenForTest.addDummyUser(userDto);
        return ResponseEntity.ok(tokenDto);
    }
}
