package com.sparta.post_board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.post_board.contorller.FeedController;
import com.sparta.post_board.contorller.UserController;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.security.WebSecurityConfig;
import com.sparta.post_board.service.FeedService;
import com.sparta.post_board.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {FeedController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class FeedControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    FeedService feedService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockUserSetup();
    }

    private void mockUserSetup() {
        String username = "test user";
        String password = "password";
        User testUser = new User(username, password, UserRoleEnum.USER);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("게시글 작성")
    void createFeedTest() throws Exception {
        // given
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        String feedInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/feeds")
                        .content(feedInfo)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 조회")
    void getFeedTest() throws Exception {
        // given
        Long feedId = 1L;
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        User user = new User("test user", "password", UserRoleEnum.USER);
        Feed feed = new Feed(requestDto, user);
        FeedResponseDto responseDto = new FeedResponseDto(feed);

        when(feedService.getFeed(feedId)).thenReturn(responseDto);

        // when - then
        mvc.perform(get("/feeds/{feedId}", feedId).principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.contents").value(responseDto.getContents()))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void updateFeedTest() throws Exception {
        // given
        Long feedId = 1L;
        FeedRequestDto requestDto = new FeedRequestDto("제목 수정", "내용 수정");
        String feedInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
       mvc.perform(put("/feeds/{feedId}", feedId)
                        .content(feedInfo)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 검색")
    void searchFeedTest() throws Exception {
        // given
        String keyword = "제목";
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        Feed feed = new Feed(requestDto, new User());

        List<FeedResponseDto> dtoList = Arrays.asList(
                new FeedResponseDto(feed), new FeedResponseDto(feed)
        );

        when(feedService.searchFeed(keyword)).thenReturn(dtoList);

        // when - then
        mvc.perform(get("/feeds/search")
                        .param("keyword", keyword)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(dtoList.get(0).getTitle()))
                .andDo(print());
    }

    @Test
    @DisplayName("할일 완료")
    void completeFeedTest() throws Exception {
        // given
        Long feedId = 1L;

        // when - then
        mvc.perform(put("/feeds/{feedId}/complete", feedId).principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(content().string("할일 완료!"))
                .andDo(print());
    }
}
