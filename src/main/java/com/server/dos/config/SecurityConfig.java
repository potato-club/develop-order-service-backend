package com.server.dos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()   // antMatchers : 권한 관리 대상 지정
                .anyRequest().permitAll();   // 나머지 URL들은 모두 인증된 사용자(로그인한 사용자)에게만 허용
//                .and()
//                .oauth2Login()
//                .defaultSuccessUrl("/login-success")        // oauth2 인증 성공 시 이동되는 url
//                .successHandler(oAuth2SuccessHandler)     // 인증 프로세스에 따라 사용자 정의 로직 실행
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService);  // 로그인이 성공하면 해당 유저의 정보를 갖고 customOAuth2UserService에서 후처리를 함
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
