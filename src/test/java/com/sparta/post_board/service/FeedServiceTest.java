package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {
    @Mock
    FeedRepository feedRepository;

    @Mock
    UserRepository userRepository;

    User user;
    Feed feed;
    FeedRequestDto dto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        dto = new FeedRequestDto("제목", "내용");
        feed = new Feed(dto, user);
    }

    @Test
    @DisplayName("피드 수정")
    void updateFeedTest1(){
        // Given
        Long feedId = 1L;
        FeedService feedService = new FeedService(feedRepository, userRepository);
        FeedRequestDto updateDto = new FeedRequestDto("제목 수정", "내용 수정");

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        FeedResponseDto responseDto = feedService.updateFeed(feedId, updateDto, user);

        // Then
        assertEquals("제목 수정", responseDto.getTitle());
        assertEquals("내용 수정", responseDto.getContents());
    }

    @Test
    @DisplayName("피드 수정 - 작성자가 아닌 경우")
    void updateFeedTest2(){
        // Given
        Long feedId = 1L;

        User user2 = new User();
        user2.setId(2L);

        FeedService feedService = new FeedService(feedRepository, userRepository);

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            feedService.updateFeed(feedId, dto, user2);
        });

        // Then
        assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("피드 숨기기")
    void blindFeedTest(){
        // Given
        Long feedId = 1L;
        FeedService feedService = new FeedService(feedRepository, userRepository);

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        feedService.blindFeed(feedId, user);

        // then
        assertTrue(feed.isBlind());
    }
}
