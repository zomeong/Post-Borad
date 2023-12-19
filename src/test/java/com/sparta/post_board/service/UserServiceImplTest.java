package com.sparta.post_board.service;

import com.sparta.post_board.dto.UserRequestDto;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.exception.DuplicateUserException;
import com.sparta.post_board.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("회원가입 성공")
    void signupTest1() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test user", "password");

        when(userRepository.findByUsername("test user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // when
        userServiceImpl.signup(requestDto);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("test user", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals(UserRoleEnum.USER, capturedUser.getRole());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 유저")
    void signupTest2() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test user", "password");
        User user = new User("test user", "password", UserRoleEnum.USER);

        when(userRepository.findByUsername("test user")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // when
        Exception e = assertThrows(DuplicateUserException.class, () -> {
            userServiceImpl.signup(requestDto);
        });

        // then
        assertEquals("중복된 사용자가 존재합니다.", e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
