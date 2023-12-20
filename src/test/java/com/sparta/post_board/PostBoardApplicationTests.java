package com.sparta.post_board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.post_board.common.PageDto;
import com.sparta.post_board.controller.MockSpringSecurityFilter;
import com.sparta.post_board.dto.*;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
import com.sparta.post_board.repository.CommentRepository;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import com.sparta.post_board.service.CommentServiceImpl;
import com.sparta.post_board.service.FeedServiceImpl;
import com.sparta.post_board.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostBoardApplicationTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FeedServiceImpl feedServiceImpl;

    @Autowired
    CommentServiceImpl commentServiceImpl;

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

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }


    @Nested
    @DisplayName("회원가입")
    class signupTest {

        @Test
        @Order(1)
        @DisplayName("회원가입 성공")
        void signupTest1() throws Exception {
            // given
            UserRequestDto requestDto = new UserRequestDto("username", "password");
            String userInfo = objectMapper.writeValueAsString(requestDto);

            // when - then
            mvc.perform(post("/user/signup")
                            .content(userInfo)
                            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                            .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("회원가입 성공"))
                    .andDo(print());
        }

        @Test
        @DisplayName("회원가입 실패 - 중복된 유저")
        void signupTest2() throws Exception {
            // given
            UserRequestDto requestDto = new UserRequestDto("username", "password");
            String userInfo = objectMapper.writeValueAsString(requestDto);

            // when - then
            mvc.perform(post("/user/signup")
                            .content(userInfo)
                            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                            .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("중복된 사용자가 존재합니다."))
                    .andDo(print());
        }
    }

    @Test
    @Order(2)
    @DisplayName("피드 작성 성공")
    void createFeedTest(){
        // given
        UserRequestDto userDto = new UserRequestDto("test user", "password");
        userServiceImpl.signup(userDto);
        user = userRepository.findById(1L).orElse(null);

        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");

        // when
        FeedResponseDto responseDto = feedServiceImpl.createFeed(requestDto, user);

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
        LinkedHashMap<String, List<FeedResponseDto>> responseList = feedServiceImpl.getAllFeeds(user);

        // then
        assertEquals(2, responseList.get("test user").size());
    }

    @Test
    @Order(4)
    @DisplayName("피드 조회 성공")
    void getFeedTest1(){
        // when
        FeedResponseDto responseDto = feedServiceImpl.getFeed(feedId);

        // then
        assertEquals("제목", responseDto.getTitle());
        assertEquals("내용", responseDto.getContents());
    }

    @Test
    @DisplayName("피드 조회 실패")
    void getFeedTest2(){
        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            feedServiceImpl.getFeed(10L);
        });

        // then
        assertEquals("피드을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("피드 검색 성공")
    void searchFeedTest1(){
        // given
        String keyword = "제";
        PageDto pageDto = new PageDto(1, 10, "createAt");

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
        String keyword = "존재하지 않는 제목";
        PageDto pageDto = new PageDto(1, 10, "createAt");

        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            feedServiceImpl.searchFeed(keyword, pageDto);
        });

        // then
        assertEquals("검색 결과을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("피드 수정 성공")
    void updateFeedTest1(){
        // Given
        FeedRequestDto updateDto = new FeedRequestDto("제목 수정", "내용 수정");

        // When
        FeedResponseDto responseDto = feedServiceImpl.updateFeed(feedId, updateDto, user);

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
        Exception e = assertThrows(OnlyAuthorAccessException.class, () -> {
            feedServiceImpl.updateFeed(feedId, updateDto, user2);
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
        CommentResponseDto responseDto = commentServiceImpl.createComment(feedId, requestDto, user);

        // Then
        assertEquals("댓글", responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 작성 실패 - 피드 없음")
    void createCommentTest(){
        // Given
        CommentRequestDto requestDto = new CommentRequestDto("댓글");

        // when
        Exception e = assertThrows(NotFoundException.class, () -> {
            commentServiceImpl.createComment(10L, requestDto, user);
        });

        // then
        assertEquals("피드을(를) 찾을 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateCommentTest1(){
        // Given
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");

        // When
        CommentResponseDto responseDto = commentServiceImpl.updateComment(feedId, commentId, requestDto, user);

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
        Exception e = assertThrows(OnlyAuthorAccessException.class, () -> {
            commentServiceImpl.updateComment(feedId, commentId, requestDto, user2);
        });

        // then
        assertEquals("작성자만 수정/삭제 가능합니다.", e.getMessage());
    }
}
