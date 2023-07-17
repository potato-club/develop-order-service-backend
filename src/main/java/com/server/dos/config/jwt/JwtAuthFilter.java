package com.server.dos.config.jwt;


import com.server.dos.exception.custom.TokenException;
import com.server.dos.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {  // 토큰 인증 처리
    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try{
            String token = request.getHeader("Authorization");

            if(token != null && jwtProvider.verifyToken(token)){
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request,response);
        }catch (TokenException e){
            String msg = e.getErrorMessage();

            if (ErrorCode.EXPIRED_TOKEN.getMessage().equals(msg)){
                sendResponse(response,ErrorCode.EXPIRED_TOKEN);
            } else if (ErrorCode.INVALID_TOKEN.getMessage().equals(msg)) {
                sendResponse(response,ErrorCode.INVALID_TOKEN);
            } else if (ErrorCode.UNSUPPORTED_TOKEN.getMessage().equals(msg)) {
                sendResponse(response,ErrorCode.UNSUPPORTED_TOKEN);
            } else if (ErrorCode.MISSING_TOKEN.getMessage().equals(msg)) {
                sendResponse(response,ErrorCode.MISSING_TOKEN);
            }
        }
    }

    private void sendResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException{
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        json.put("code",errorCode.getCode());
        json.put("message",errorCode.getMessage());
        response.getWriter().print(json);
    }
}
