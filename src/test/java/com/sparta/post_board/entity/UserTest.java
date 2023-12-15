package com.sparta.post_board.entity;

import com.sparta.post_board.dto.UserRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class UserTest {

    @Test
    void testUserEntity(){
        // given
        User user = new User();

        // when
        user.setUsername("testuser");

        // then
        assertEquals("testuser", user.getUsername());
    }

    @Test
    @DisplayName("유저 request dto 생성 성공")
    void testUserRequestDto1(){
        // given
        UserRequestDto requestDto = new UserRequestDto("testuser", "password");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("유저 request dto 생성 실패 - 잘못된 username")
    void testUserRequestDto2(){
        // given
        UserRequestDto requestDto = new UserRequestDto("sa", "password");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("사용자 이름은 4자 이상 10자 이하여야 합니다.");
    }

    @Test
    @DisplayName("유저 request dto 생성 실패 - 잘못된 password")
    void testUserRequestDto3(){
        // given
        UserRequestDto requestDto = new UserRequestDto("testuser", "password!!!");

        // when
        Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

        // then
        assertThat(violations).hasSize(1);
    }

    private Set<ConstraintViolation<UserRequestDto>> validate(UserRequestDto userRequestDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userRequestDTO);
    }
}
