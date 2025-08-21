package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j; // Slf4j 임포트 추가

@Service
@RequiredArgsConstructor
@Slf4j // 로그를 사용하기 위한 어노테이션 추가
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>>>>> UserDetailsService: 사용자 찾기 시도 - {}", username);
        return userRepository.findByUserid(username)
                .map(user -> {
                    log.info(">>>>>> UserDetailsService: 사용자 찾음! - {}", user.getUserid());
                    return user;
                })
                .orElseThrow(() -> {
                    log.error(">>>>>> UserDetailsService: 사용자를 찾을 수 없음 - {}", username);
                    return new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + username);
                });
    }
}
