package com.sparta.post_board.entity;

import com.sparta.post_board.dto.CommentRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class CommentTest {

    Comment comment = new Comment();

    @Test
    void commentEntityTest(){
        // When
        comment.setContents("댓글");

        // Then
        assertEquals("댓글", comment.getContents());
    }

    @Test
    void commentUpdateTest(){
        // Given
        CommentRequestDto dto = new CommentRequestDto("댓글 수정");

        // When
        comment.update(dto);

        // Then
        assertEquals("댓글 수정", comment.getContents());
    }
}
