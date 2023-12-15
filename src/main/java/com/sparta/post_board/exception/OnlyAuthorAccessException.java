package com.sparta.post_board.exception;

public class OnlyAuthorAccessException extends RuntimeException {

    private static final String message = "작성자만 수정/삭제 가능합니다.";

    public OnlyAuthorAccessException(){
        super(message);
    }
}
