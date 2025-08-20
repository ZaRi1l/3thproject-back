package com._thproject._thproject_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정을 활성화합니다.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호를 비활성화합니다. (API 서버에서는 보통 비활성화합니다)
                .authorizeHttpRequests(authorize -> authorize
                        // 아래에 명시된 URL 경로는 인증 없이 누구나 접근할 수 있도록 허용합니다.
                        .requestMatchers(
                            "/",
                            "/v3/api-docs/**",          // Swagger API docs
                            "/swagger-ui/**",            // Swagger UI
                            "/swagger-ui.html",           // Swagger UI entry point
                            "/api/**",
                            "/graphql",
                            "/graphiql/**"
                        ).permitAll()
                        // 위에서 허용한 URL을 제외한 모든 요청은 인증을 받아야 합니다.
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable()) // Spring Security의 기본 폼 로그인을 비활성화합니다.
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증을 비활성화합니다.

        return http.build();
    }
}
