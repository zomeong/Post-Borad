package com.sparta.post_board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER(Authority.USER);  // 사용자 권한

    private final String authority;

    public static class Authority {
        public static final String USER = "ROLE_USER";
    }
}
