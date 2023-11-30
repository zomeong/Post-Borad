package com.sparta.post_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedRequestDto {
    private String title;
    private String contents;
}
