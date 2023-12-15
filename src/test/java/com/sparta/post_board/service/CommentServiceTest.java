package com.sparta.post_board.service;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
import com.sparta.post_board.repository.CommentRepository;
import com.sparta.post_board.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    FeedRepository feedRepository;

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    User user;
    Feed feed;
    Comment comment;
    FeedRequestDto feedDto;
    CommentRequestDto commentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        feedDto = new FeedRequestDto("제목", "내용");
        feed = new Feed(feedDto, user);

        commentDto = new CommentRequestDto("댓글");
        comment = new Comment(commentDto, feed, user);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void createCommentTest1(){
        // Given
        when(feedRepository.findById(1L)).thenReturn(Optional.of(feed));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDto responseDto = commentService.createComment(1L, commentDto, user);

        // Then
        assertEquals("댓글", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 작성 실패 - 피드 없음")
    void createCommentTest(){
        // given
        when(feedRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            commentService.createComment(1L, commentDto, user);
        });

        // then
        assertEquals("피드을(를) 찾을 수 없습니다.", e.getMessage());
     }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateCommentTest1(){
        // Given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        when(commentRepository.findByIdAndFeedId(1L, 1L)).thenReturn(Optional.of(comment));

        // When
        CommentResponseDto responseDto = commentService.updateComment(1L, 1L, requestDto, user);

        // Then
        assertEquals("댓글 수정", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 댓글 없음")
    void updateCommentTest2(){
        // given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        when(commentRepository.findByIdAndFeedId(1L, 1L)).thenReturn(Optional.empty());

        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            commentService.updateComment(1L, 1L, requestDto, user);
        });

        // then
        assertEquals("댓글을(를) 찾을 수 없습니다.", e.getMessage());
     }

     @Test
     @DisplayName("댓글 수정 실패 - 작성자가 아닌 경우")
     void updateCommentTest3(){
         // given
         User user2 = new User("test user2", "password", UserRoleEnum.USER);
         CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
         when(commentRepository.findByIdAndFeedId(1L, 1L)).thenReturn(Optional.of(comment));

         // when
         Exception e = assertThrows(OnlyAuthorAccessException.class, () -> {
             commentService.updateComment(1L, 1L, requestDto, user2);
         });

         // then
         assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
     }
}
