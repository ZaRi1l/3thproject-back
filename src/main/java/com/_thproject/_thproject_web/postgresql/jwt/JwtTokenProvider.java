// src/main/java/com/_thproject/_thproject_web/postgresql/jwt/JwtTokenProvider.java

package com._thproject._thproject_web.postgresql.jwt;

import com._thproject._thproject_web.postgresql.dto.TokenDto;
import com._thproject._thproject_web.postgresql.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// --- [핵심 수정 1] ---
// SecretKey를 import 합니다.
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    // --- [핵심 수정 2] ---
    // 변수 타입을 Key에서 SecretKey로 변경합니다.
    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Keys.hmacShaKeyFor는 SecretKey를 반환하므로 타입이 일치합니다.
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Object principal = authentication.getPrincipal();
        String realUsername = "";

        // Principal이 우리가 만든 User 타입인지 명확하게 확인합니다.
        if (principal instanceof com._thproject._thproject_web.postgresql.entity.User) {
            // 맞다면, User 엔티티로 형변환 후 getRealUsername() 메소드를 호출합니다.
            realUsername = ((com._thproject._thproject_web.postgresql.entity.User) principal).getRealUsername();
        } else if (principal instanceof UserDetails) {
            // 만약 다른 UserDetails 타입이라면, 일단 Spring Security의 getUsername()을 사용합니다.
            // (이 경우 userid가 들어갈 수 있습니다)
            realUsername = ((UserDetails) principal).getUsername();
        }

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 3600000); // 1시간

        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .claim("username", realUsername)
                .expiration(accessTokenExpiresIn) // setExpiration -> expiration으로 변경 (최신 권장)
                .signWith(key) // signWith(key, SignatureAlgorithm)은 deprecated. key만 넘기는 것이 권장됨
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            // 이제 verifyWith(key)는 SecretKey 타입의 인자를 받으므로 오류가 없습니다.
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 모든 JWT 관련 예외를 한 번에 처리
            log.info("Invalid JWT Token: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}