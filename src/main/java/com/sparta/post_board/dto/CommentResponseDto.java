package com.sparta.post_board.dto;

import com.sparta.post_board.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String contents;

    public CommentResponseDto(Comment comment){
        this.contents = comment.getContents();
    }
}
