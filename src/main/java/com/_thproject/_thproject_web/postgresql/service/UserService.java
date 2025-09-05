package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.dto.UserCreateRequestDto;
import com._thproject._thproject_web.postgresql.dto.UserResponseDto;
import com._thproject._thproject_web.postgresql.entity.User;
import com._thproject._thproject_web.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com._thproject._thproject_web.postgresql.dto.UserResponseDto;
import com._thproject._thproject_web.postgresql.entity.User;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        // 사용자 ID 중복 확인
        if (userRepository.findByUserid(requestDto.getUserid()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다: " + requestDto.getUserid());
        }

        // User 엔티티 생성
        User newUser = User.builder()
                .userid(requestDto.getUserid())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword())) // 비밀번호 암호화
                .userRole(requestDto.getUserRole())
                .build();

        // DB에 저장
        User savedUser = userRepository.save(newUser);

        // 응답 DTO로 변환하여 반환
        return UserResponseDto.from(savedUser);
    }
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::from) // User 엔티티를 UserResponseDto로 변환
                .collect(Collectors.toList());
    }
}
