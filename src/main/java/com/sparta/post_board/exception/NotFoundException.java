package com.sparta.post_board.exception;

public class NotFoundException extends RuntimeException{
    private static final String message = "을(를) 찾을 수 없습니다.";

    public NotFoundException(String s){
        super(s + message);
    }
}
