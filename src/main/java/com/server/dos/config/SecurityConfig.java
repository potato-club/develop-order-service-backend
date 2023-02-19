package com.server.dos.config;

import com.server.dos.OAuth2SuccessHandler;
import com.server.dos.config.jwt.JwtAuthFilter;
import com.server.dos.config.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtProvider jwtProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()   // antMatchers : 권한 관리 대상 지정
                .anyRequest().authenticated()   // 나머지 URL들은 모두 인증된 사용자(로그인한 사용자)에게만 허용
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .defaultSuccessUrl("/login-success")        // oauth2 인증 성공 시 이동되는 url
                .successHandler(oAuth2SuccessHandler)     // 인증 프로세스에 따라 사용자 정의 로직 실행
                .userInfoEndpoint()
                .userService(customOAuth2UserService);  // 로그인이 성공하면 해당 유저의 정보를 갖고 customOAuth2UserService에서 후처리를 함

            http.addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
