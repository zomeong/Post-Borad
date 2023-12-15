package com.sparta.post_board.exception;

public class DuplicateUserException extends RuntimeException {
    private static final String message = "중복된 유저입니다.";

    public DuplicateUserException(){
        super( message);
    }
}
