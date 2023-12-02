package com.sparta.post_board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.post_board.contorller.CommentController;
import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.security.WebSecurityConfig;
import com.sparta.post_board.service.CommentService;
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
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class CommentControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

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
    @DisplayName("댓글 작성")
    void createCommentTest() throws Exception {
        // given
        Long feedId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글");
        String commentInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/feeds/{feedId}/comments", feedId)
                        .content(commentInfo)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateCommentTest() throws Exception{
        // given
        Long feedId = 1L;
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");
        String commentInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(put("/feeds/{feedId}/comments/{commentId}", feedId, commentId)
                        .content(commentInfo)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() throws Exception{
        // given
        Long feedId = 1L;
        Long commentId = 1L;

        // when - then
        mvc.perform(delete("/feeds/{feedId}/comments/{commentId}", feedId, commentId)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("댓글이 삭제되었습니다."))
                .andDo(print());
    }
}
