package com.sparta.post_board.service;

import com.sparta.post_board.dto.UserRequestDto;

public interface UserService {

    /**
     * 회원가입
     * @param requestDto 회원가입 요청 정보
     */
    void signup(UserRequestDto requestDto);
}
