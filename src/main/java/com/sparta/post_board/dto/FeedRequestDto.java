package com.sparta.post_board.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class FeedRequestDto {
    private String password;
    private String username;
    private String title;
    private String contents;
}
