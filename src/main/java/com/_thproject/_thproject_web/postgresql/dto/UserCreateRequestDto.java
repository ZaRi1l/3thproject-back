package com._thproject._thproject_web.postgresql.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    private String userid;
    private String username;
    private String password; // 평문 비밀번호
    private String userRole; // 예: "USER", "DOCTOR" 등
}
