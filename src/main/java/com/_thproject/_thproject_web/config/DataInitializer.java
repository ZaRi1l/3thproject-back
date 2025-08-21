package com._thproject._thproject_web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String rawPassword = "password1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("====================================================================");
        System.out.println("임시 비밀번호 암호화 결과");
        System.out.println("원본 비밀번호: " + rawPassword);
        System.out.println("암호화된 비밀번호: " + encodedPassword);
        System.out.println("====================================================================");
    }
}
