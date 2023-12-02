package com.sparta.post_board.repository;

import com.sparta.post_board.JpaConfig;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaConfig.class)
public class FeedRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FeedRepository feedRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("test user", "password", UserRoleEnum.USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("사용자의 완료되지 않은 피드 최신순 조회")
    void findAllTest(){
        // given
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        Feed feed1 = new Feed(requestDto, user);
        Feed feed2 = new Feed(requestDto, user);
        Feed feed3 = new Feed(requestDto, user);
        feedRepository.saveAll(List.of(feed1, feed2, feed3));

        feed3.setComplete(true);

        // when
        List<Feed> feedList = feedRepository.findAllByUserAndCompleteOrderByCreatedAtDesc(user, false);

        // then
        assertThat(feedList).containsExactly(feed2, feed1);
    }

    @Test
    @DisplayName("제목으로 피드를 찾아 최신순 조회")
    void findByTitleTest(){
        // given
        FeedRequestDto requestDto = new FeedRequestDto("제목", "내용");
        Feed feed1 = new Feed(requestDto, user);
        Feed feed2 = new Feed(requestDto, user);
        Feed feed3 = new Feed(requestDto, user);
        feedRepository.saveAll(List.of(feed1, feed2, feed3));

        // when
        List<Feed> feedList = feedRepository.findByTitleOrderByCreatedAtDesc("제목");

        // then
        assertThat(feedList).containsExactly(feed3, feed2, feed1);
    }
}
