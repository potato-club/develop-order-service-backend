package com.server.dos.config.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {  // 토큰 인증 처리
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Authorization");
        log.info("jwt filter에서 token: "+token);

        if(token != null && jwtProvider.verifyToken(token)){
            log.info("들어옴");

            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("들어오지 못함");
        chain.doFilter(request,response);
    }


}
