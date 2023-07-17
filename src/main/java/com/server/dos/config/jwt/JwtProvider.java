package com.server.dos.config.jwt;

import com.server.dos.dto.TokenDto;
import com.server.dos.exception.custom.TokenException;
import com.server.dos.exception.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.servlet.ServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {  // 토큰 인증 및 검증
    private final Key key;
    private final long accessExpire = 1000 * 60 * 10; // 30분
    private final long refreshExpire = 1000 * 60 * 60 * 24 * 14; // 2주
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public JwtProvider(@Value("${jwt.secret}") String secretKey){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public TokenDto generateToken(String uid, String role){

        Claims claims = Jwts.claims().setSubject(uid); // sub(subject) : 토큰제목
        claims.put("role",role);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)                                      // payload "role": "ROLE_USER"
                .setExpiration(new Date(now.getTime() + accessExpire))  // payload "exp" : 14234532(예시)
                .signWith(key)                 // header  "alg" : "HS256"
                .compact();

        String refreshToken = refreshToken(uid,role);

        log.info("AccessToken : " + accessToken);
        log.info("RefreshToken : " + refreshToken);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String refreshToken(String uid,String role){
        Claims claims = Jwts.claims().setSubject(uid); // sub(subject) : 토큰제목
        claims.put("role",role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + refreshExpire))
                .signWith(key)
                .compact();
    }


    public boolean verifyToken(String token, ServletRequest request){
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
//            throw new TokenException(ErrorCode.EXPIRED_TOKEN,"토큰 기한이 만료되었습니다.");
            logger.error("JWT token is expired: {}","토큰 기한이 만료되었습니다.");
        }catch (UnsupportedJwtException e){
//            throw new TokenException(ErrorCode.INVALID_TOKEN,"예상하는 형식과 일치하지 않는 특정 형식이나 구성의 토큰입니다.");
            logger.error("JWT token is unsupported: {}",e.getMessage());
        }catch (SignatureException e){
//            throw new TokenException(ErrorCode.INVALID_TOKEN,"사용자 인증을 실패하였습니다..");
            logger.error("Invalid JWT signature: {}",e.getMessage());
        }catch (MalformedJwtException e){
//            throw new TokenException(ErrorCode.INVALID_TOKEN,"올바른 JWT 구성이 아닙니다.");
            logger.error("Invalid JWT signature: {}",e.getMessage());
        }catch (IllegalArgumentException e){
//            throw new TokenException(ErrorCode.UNAUTHORIZED,e.getMessage());
            logger.error("JWT claims string is empty: {}",e.getMessage());
        }
        return false;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메소드
    public Authentication getAuthentication(String token){

        // 토큰 복호화
        Claims claims = parseClaims(token);
        log.info("claims: "+ claims);

        if(claims.get("role") == null){
            throw new RuntimeException("Not Authorization");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        log.info("authorities: " + authorities);

        UserDetails userDetails = new User(claims.getSubject(),"",authorities);
        return new UsernamePasswordAuthenticationToken(userDetails,"",authorities);
    }

    public Claims parseClaims(String token){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public String getUid(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setHeader("Authorization",accessToken);
//        response.setHeader("Refresh",refreshToken);
//
//        log.info("Header 설정 완료");
//
//    }
}
