package com.sparta.post_board.entity;

import com.sparta.post_board.dto.FeedRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class FeedTest {

    Feed feed = new Feed();

    @Test
    void feedEntityTest(){
        // When
        feed.setTitle("제목");
        feed.setContents("내용");

        // Then
        assertEquals("제목", feed.getTitle());
        assertEquals("내용", feed.getContents());
    }

    @Test
    void feedUpdateTest(){
        // Given
        FeedRequestDto dto = new FeedRequestDto("제목 수정", "내용 수정");

        // When
        feed.update(dto);

        // Then
        assertEquals("제목 수정", feed.getTitle());
        assertEquals("내용 수정", feed.getContents());
    }
}
