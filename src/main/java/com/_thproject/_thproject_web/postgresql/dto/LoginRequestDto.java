package com._thproject._thproject_web.postgresql.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String userid;
    private String password;
}
