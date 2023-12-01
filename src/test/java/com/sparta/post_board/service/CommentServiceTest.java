package com.sparta.post_board.service;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.repository.CommentRepository;
import com.sparta.post_board.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    FeedRepository feedRepository;

    @Mock
    CommentRepository commentRepository;

    User user;
    Feed feed;
    Comment comment;
    FeedRequestDto feedDto;
    CommentRequestDto CommentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        feedDto = new FeedRequestDto("제목", "내용");
        feed = new Feed(feedDto, user);

        CommentDto = new CommentRequestDto("댓글");
        comment = new Comment(CommentDto, feed, user);
    }

    @Test
    @DisplayName("댓글 작성")
    void createCommentTest(){
        // Given
        CommentService commentService = new CommentService(commentRepository, feedRepository);

        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDto responseDto = commentService.createComment(1L, CommentDto, user);

        // Then
        assertEquals("댓글", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateCommentTest(){
        // Given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        CommentService commentService = new CommentService(commentRepository, feedRepository);

        when(commentRepository.findByIdAndFeedId(1L, 1L)).thenReturn(Optional.of(comment));

        // When
        CommentResponseDto responseDto = commentService.updateComment(1L, 1L, requestDto, user);

        // Then
        assertEquals("댓글 수정", responseDto.getContents());
    }
}
