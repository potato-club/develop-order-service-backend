package com.server.dos.config.jwt;


import com.server.dos.exception.custom.TokenException;
import com.server.dos.exception.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends GenericFilter {  // 토큰 인증 처리
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        try{
            String token = ((HttpServletRequest) request).getHeader("Authorization");

            if(token != null && jwtProvider.verifyToken(token)){
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (ExpiredJwtException e){
            throw new TokenException(ErrorCode.EXPIRED_TOKEN,ErrorCode.EXPIRED_TOKEN.getMessage());
        }catch (UnsupportedJwtException e){
            throw new TokenException(ErrorCode.INVALID_TOKEN,"예상하는 형식과 일치하지 않는 특정 형식이나 구성의 토큰입니다.");
        }catch (SignatureException e){
            throw new TokenException(ErrorCode.INVALID_TOKEN,"사용자 인증을 실패하였습니다..");
        }catch (MalformedJwtException e){
            throw new TokenException(ErrorCode.INVALID_TOKEN,"올바른 JWT 구성이 아닙니다.");
        }catch (IllegalArgumentException e){
            throw new TokenException(ErrorCode.UNAUTHORIZED,e.getMessage());
        }
        chain.doFilter(request,response);
    }
}
