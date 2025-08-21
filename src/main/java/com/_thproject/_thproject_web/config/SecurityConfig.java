package com._thproject._thproject_web.config;

import com._thproject._thproject_web.postgresql.jwt.JwtAuthenticationFilter;
import com._thproject._thproject_web.postgresql.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
public class SecurityConfig {

    // 1. [추가] JwtTokenProvider를 주입받습니다.
    private final JwtTokenProvider jwtTokenProvider;

    // 2. [추가] PasswordEncoder를 Bean으로 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // 기존의 인증 없이 접근 가능한 경로들
                        .requestMatchers(
                                "/auth/**",
                                "/",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/graphql",
                                "/graphiql/**",
                                "/api/images/**"
                        ).permitAll()
                        // ▼▼▼▼▼ [핵심] 이 부분을 추가합니다 ▼▼▼▼▼
                        // /api/admin/으로 시작하는 모든 경로는 'ADMIN' 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 위에서 설정한 경로 외의 모든 요청은 인증이 필요함
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
