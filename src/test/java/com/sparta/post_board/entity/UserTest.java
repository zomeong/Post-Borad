package com.sparta.post_board.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class UserTest {

    @Test
    void testUserEntity(){
        User user = new User();
        user.setUsername("test user");
        assertEquals("test user", user.getUsername());
    }
}
