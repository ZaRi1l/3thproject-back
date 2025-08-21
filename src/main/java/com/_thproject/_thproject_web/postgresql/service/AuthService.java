package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.dto.LoginRequestDto;
import com._thproject._thproject_web.postgresql.dto.TokenDto;
import com._thproject._thproject_web.postgresql.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
// [추가] AuthenticationManager 임포트
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// [삭제] AuthenticationManagerBuilder는 더 이상 필요 없음
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j; // Slf4j 임포트 추가

@Service
@RequiredArgsConstructor
@Slf4j // 로그를 사용하기 위한 어노테이션 추가
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUserid(), loginRequestDto.getPassword());

        Authentication authentication = null;
        try {
            log.info(">>>>>> AuthService: 인증 시도 - 사용자 ID: {}", authenticationToken.getName());
            authentication = authenticationManager.authenticate(authenticationToken);
            log.info(">>>>>> AuthService: 인증 성공!");
        } catch (Exception e) {
            // 어떤 종류의 인증 에러가 발생했는지 로그로 확인!
            log.error(">>>>>> AuthService: 인증 실패 - 에러 타입: {}, 메시지: {}", e.getClass().getSimpleName(), e.getMessage());
            throw e; // 예외를 다시 던져서 403 응답이 나가도록 함
        }

        return jwtTokenProvider.generateToken(authentication);
    }
}
