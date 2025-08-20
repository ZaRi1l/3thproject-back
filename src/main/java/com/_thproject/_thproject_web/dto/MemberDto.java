package com._thproject._thproject_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDto {
    private String username;
    private String email;

    @Getter
    @AllArgsConstructor 
    public static class MemberRequestDto {
        private String username;
        private String email;
    }
}
