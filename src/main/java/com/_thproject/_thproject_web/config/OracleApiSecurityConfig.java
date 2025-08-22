package com._thproject._thproject_web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// spring.datasource.oracle.enabled=true 일 때만 이 보안 설정 파일이 활성화됩니다.
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
public class OracleApiSecurityConfig {

    @Bean
    // @Order(1)을 사용하여 다른 SecurityFilterChain보다 먼저 적용되도록 우선순위를 부여합니다.
    // 이렇게 하면 /api/images/** 요청은 이 필터 체인에서 먼저 처리됩니다.
    @Order(1)
    public SecurityFilterChain oracleApiFilterChain(HttpSecurity http) throws Exception {
        http
                // 이 보안 설정은 "/api/images/**" 경로에만 적용됩니다.
                .securityMatcher("/api/images/**")
                .authorizeHttpRequests(auth -> auth
                        // /api/images/ 하위 모든 경로는 인증 없이 허용합니다. (기존 설정과 동일)
                        .anyRequest().permitAll()
                )
                // 이 필터 체인은 특정 경로에만 적용되므로, 다른 설정(csrf, cors 등)은 생략해도 됩니다.
                // 메인 SecurityConfig의 설정이 전역적으로 적용되기 때문입니다.
                .csrf(csrf -> csrf.disable());


        return http.build();
    }
}