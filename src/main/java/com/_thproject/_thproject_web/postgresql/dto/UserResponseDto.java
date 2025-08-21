package com._thproject._thproject_web.postgresql.dto;

import com._thproject._thproject_web.postgresql.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private String userid;
    private String username;
    private String userRole;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userid(user.getUserid())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .build();
    }
}
