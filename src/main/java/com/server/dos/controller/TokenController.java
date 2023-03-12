package com.server.dos.controller;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private JwtProvider jwtProvider;

    @GetMapping("/token/expired")
    public String auth(){
        throw new RuntimeException();
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

            return "NEW TOKEN";
        }

        throw new RuntimeException();
    }
}
