package com.server.dos.config;

import com.server.dos.controller.OAuth2SuccessHandler;
import com.server.dos.config.jwt.JwtAuthFilter;
import com.server.dos.config.jwt.JwtProvider;
import com.server.dos.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtProvider jwtProvider;
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
                .antMatchers("/api/doc", "/swagger-ui/*", "/swagger-resources/**",
                        "/swagger-ui.html", "/webjars/**", "/v3/api-docs").permitAll()
                .antMatchers("/token/dummy","/").permitAll()
                .antMatchers(HttpMethod.GET, "/orders/main", "/orders/detail","/admin/info").permitAll() // antMatchers : 권한 관리 대상 지정
                .antMatchers("/admin/signup","/admin/login").permitAll()
                .anyRequest().authenticated()   // 나머지 URL들은 모두 인증된 사용자(로그인한 사용자)에게만 허용
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().loginPage("/")
                .defaultSuccessUrl("/")        // oauth2 인증 성공 시 이동되는 url
                .successHandler(oAuth2SuccessHandler)     // 인증 프로세스에 따라 사용자 정의 로직 실행
                .userInfoEndpoint()        //OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정등을 담당
                .userService(customOAuth2UserService);  // 로그인이 성공하면 해당 유저의 정보를 갖고 customOAuth2UserService에서 후처리를 함

        http.addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
