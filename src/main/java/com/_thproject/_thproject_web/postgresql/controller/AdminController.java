package com._thproject._thproject_web.postgresql.controller;

import com._thproject._thproject_web.postgresql.dto.UserCreateRequestDto;
import com._thproject._thproject_web.postgresql.dto.UserResponseDto;
import com._thproject._thproject_web.postgresql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // POST /api/admin/users
    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
