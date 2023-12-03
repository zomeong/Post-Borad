package com.sparta.post_board;

import com.sparta.post_board.dto.*;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.repository.CommentRepository;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import com.sparta.post_board.service.CommentService;
import com.sparta.post_board.service.FeedService;
import com.sparta.post_board.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostBoardApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;
    Long feedId = 1L;

    @Test
    @Order(1)
    @DisplayName("회원가입 성공")
    void signupTest1() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test user", "password");

        // when
        userService.signup(requestDto);
        user = userRepository.findById(1L).orElse(null);

        // then
        assertNotNull(user);
        assertEquals("test user", user.getUsername());
        assertEquals(UserRoleEnum.USER, user.getRole());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 유저")
    void signupTest2() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test user", "password");

        // when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertEquals("중복된 사용자가 존재합니다.", e.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("피드 작성 성공")
    void createFeedTest(){
        // given
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");

        // when
        FeedResponseDto responseDto = feedService.createFeed(requestDto, user);

        // then
        assertEquals("제목", responseDto.getTitle());
        assertEquals("내용", responseDto.getContents());
    }

    @Test
    @Order(3)
    @DisplayName("피드 전체 조회")
    void getAllFeedsTest(){
        // given
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        Feed feed2 = new Feed(requestDto, user);
        feedRepository.save(feed2);

        // when
        LinkedHashMap<String, List<FeedResponseDto>> responseList = feedService.getAllFeeds(user);

        // then
        assertEquals(2, responseList.get("test user").size());
    }

    @Test
    @Order(4)
    @DisplayName("피드 조회 성공")
    void getFeedTest1(){
        // when
        FeedResponseDto responseDto = feedService.getFeed(feedId);

        // then
        assertEquals("제목", responseDto.getTitle());
        assertEquals("내용", responseDto.getContents());
    }

    @Test
    @DisplayName("피드 조회 실패")
    void getFeedTest2(){
        // when
        Exception e = assertThrows(NullPointerException.class, () -> {
            feedService.getFeed(10L);
        });

        // then
        assertEquals("해당 피드는 존재하지 않습니다.", e.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("피드 검색 성공")
    void searchFeedTest1(){
        // given
        String keyword = "제목";

        // when
        List<FeedResponseDto> responseList = feedService.searchFeed(keyword);

        // then
        assertThat(responseList).hasSize(2);
        assertEquals("제목", responseList.get(0).getTitle());
        assertEquals("제목", responseList.get(1).getTitle());
    }

    @Test
    @DisplayName("피드 검색 실패")
    void searchFeedTest2(){
        // given
        String keyword = "존재하지 않는 제목";

        // when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            feedService.searchFeed(keyword);
        });

        // then
        assertEquals("검색 결과가 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("피드 수정 성공")
    void updateFeedTest1(){
        // Given
        FeedRequestDto updateDto = new FeedRequestDto("제목 수정", "내용 수정");

        // When
        FeedResponseDto responseDto = feedService.updateFeed(feedId, updateDto, user);

        // Then
        assertEquals("제목 수정", responseDto.getTitle());
        assertEquals("내용 수정", responseDto.getContents());
    }

    @Test
    @Order(6)
    @DisplayName("피드 수정 실패 - 작성자가 아닌 경우")
    void updateFeedTest2(){
        // Given
        User user2 = new User("test user2", "password", UserRoleEnum.USER);
        userRepository.save(user2);
        FeedRequestDto updateDto = new FeedRequestDto("제목 수정", "내용 수정");

        // When
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            feedService.updateFeed(feedId, updateDto, user2);
        });

        // Then
        assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("댓글 작성 성공")
    void createCommentTest1(){
        // Given
        CommentRequestDto requestDto = new CommentRequestDto("댓글");

        // When
        CommentResponseDto responseDto = commentService.createComment(feedId, requestDto, user);

        // Then
        assertEquals("댓글", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 작성 실패 - 피드 없음")
    void createCommentTest(){
        // Given
        CommentRequestDto requestDto = new CommentRequestDto("댓글");

        // when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(10L, requestDto, user);
        });

        // then
        assertEquals("선택한 피드가 존재하지 않습니다.", e.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateCommentTest1(){
        // Given
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");

        // When
        CommentResponseDto responseDto = commentService.updateComment(feedId, commentId, requestDto, user);

        // Then
        assertEquals("댓글 수정", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자가 아닌 경우")
    void updateCommentTest2(){
        // given
        Long commentId = 1L;
        User user2 = userRepository.findById(2L).orElse(null);
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");

        // when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(feedId, commentId, requestDto, user2);
        });

        // then
        assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
    }
}
