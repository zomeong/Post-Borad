package com.sparta.post_board.repository;

import com.sparta.post_board.config.JpaConfig;
import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaConfig.class)
public class CommentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 id와 피드 id로 댓글 찾기")
    void findCommentTest(){
        // given
        User user = new User("test user", "password", UserRoleEnum.USER);
        userRepository.save(user);

        Feed feed = new Feed(new FeedRequestDto("제목", "내용"), user);
        CommentRequestDto requestDto = new CommentRequestDto("댓글");
        Comment comment = new Comment(requestDto, feed, user);
        feedRepository.save(feed);
        commentRepository.save(comment);

        Long commentId = 1L;
        Long feedId = 1L;

        // when
        Optional<Comment> findComment = commentRepository.findByIdAndFeedId(commentId, feedId);

        // then
        assertNotNull(findComment);
    }
}
