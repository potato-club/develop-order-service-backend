package com.server.dos;

import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.dto.TokenDto;
import com.server.dos.dto.UserDto;
import com.server.dos.mapper.UserOauthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
@CrossOrigin(origins = "*" , exposedHeaders = "**")
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserOauthMapper userRequestMapper;
    private final JwtProvider jwtProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = userRequestMapper.getUser(oAuth2User);

        log.info("Principal에서 꺼낸 OAuth2User = {}",oAuth2User);

        // 최초 로그인이라면 회원가입 처리
        String tagUrl;
        log.info("토큰 발행 시작");

        TokenDto token = jwtProvider.generateToken(userDto.getEmail(),"USER");


        tagUrl = UriComponentsBuilder.fromUriString("/home")
                .queryParam("token","token")
                        .build().toUriString();
        getRedirectStrategy().sendRedirect(request,response,tagUrl);
    }
}
