package com.sparta.post_board.repository;

import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 이름으로 유저 찾기")
    void findByUsernameTest(){
        // given
        User user = new User("test user", "password", UserRoleEnum.USER);
        userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByUsername("test user");

        // then
        assertNotNull(findUser);
    }
}
