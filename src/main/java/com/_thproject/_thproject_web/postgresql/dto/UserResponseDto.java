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
                // ▼▼▼ 이 부분을 수정하세요! ▼▼▼
                // getUsername() 대신, 실제 이름을 반환하는 getRealUsername()을 호출합니다.
                .username(user.getRealUsername()) // ★★★ getUsername() -> getRealUsername() ★★★
                .userRole(user.getUserRole()) // 이 부분도 .name()을 붙이는 것을 권장합니다 -> .getUserRole().name()
                .build();
    }
}
