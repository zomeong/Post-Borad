package com.sparta.post_board.service;

import com.sparta.post_board.common.PageDto;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FeedServiceImplTest {
    @Mock
    FeedRepository feedRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FeedServiceImpl feedServiceImpl;

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
    @DisplayName("피드 작성")
    void createFeedTest(){
        // given
        when(feedRepository.save(any(Feed.class))).thenReturn(feed);

        // when
        FeedResponseDto responseDto = feedServiceImpl.createFeed(dto, user, null);

        // then
        assertEquals("제목", responseDto.getTitle());
        assertEquals("내용", responseDto.getContents());
     }

    @Test
    @DisplayName("피드 수정 성공")
    void updateFeedTest1(){
        // Given
        Long feedId = 1L;
        FeedRequestDto updateDto = new FeedRequestDto("제목 수정", "내용 수정");

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        FeedResponseDto responseDto = feedServiceImpl.updateFeed(feedId, updateDto, user);

        // Then
        assertEquals("제목 수정", responseDto.getTitle());
        assertEquals("내용 수정", responseDto.getContents());
    }

    @Test
    @DisplayName("피드 수정 실패 - 작성자가 아닌 경우")
    void updateFeedTest2(){
        // Given
        Long feedId = 1L;
        User user2 = new User();
        user2.setId(2L);

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        Exception e = assertThrows(OnlyAuthorAccessException.class, () -> {
            feedServiceImpl.updateFeed(feedId, dto, user2);
        });

        // Then
        assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
    }

    @Test
    @DisplayName("피드 조회 성공")
    void getFeedTest(){
        // given
        Long feedId = 1L;
        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // when
        FeedResponseDto responseDto = feedServiceImpl.getFeed(feedId);

        // then
        assertEquals("제목", responseDto.getTitle());
        assertEquals("내용", responseDto.getContents());
     }

     @Test
     @DisplayName("피드 검색 성공")
     void searchFeedTest1(){
         // given
         String keyword = "제";
         Feed feed1 =  new Feed(dto, user);
         Feed feed2 =  new Feed(dto, user);
         PageDto pageDto = new PageDto(1, 10, "createAt");

         when(feedRepository.search(keyword, pageDto.toPageable())).thenReturn(
                 new PageImpl<>(List.of(feed1, feed2), pageDto.toPageable(), 1));

         // when
         Page<FeedResponseDto> responseList = feedServiceImpl.searchFeed(keyword, pageDto);

         // then
         assertThat(responseList).hasSize(2);
         assertEquals("제목", responseList.getContent().get(0).getTitle());
         assertEquals("제목", responseList.getContent().get(1).getTitle());
      }

    @Test
    @DisplayName("피드 검색 실패")
    void searchFeedTest2(){
        // given
        String keyword = "제목";
        PageDto pageDto = new PageDto(1, 10, "createAt");

        when(feedRepository.search(keyword, pageDto.toPageable())).thenReturn(
                new PageImpl<>(Collections.emptyList(), pageDto.toPageable(), 1));

        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            feedServiceImpl.searchFeed(keyword, pageDto);
        });

        // then
        assertEquals("검색 결과을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("피드 숨기기")
    void blindFeedTest(){
        // Given
        Long feedId = 1L;
        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        feedServiceImpl.blindFeed(feedId, user);

        // then
        assertTrue(feed.isBlind());
    }

    @Test
    @DisplayName("피드 완료")
    void completeFeedTest(){
        // Given
        Long feedId = 1L;
        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // When
        feedServiceImpl.completeFeed(feedId, user);

        // then
        assertTrue(feed.isComplete());
    }
}
