package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.UserRequestDto;
import com.sparta.post_board.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/user/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        // bindingResult에 에러가 담김
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if(!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }

        userServiceImpl.signup(requestDto);

        return ResponseEntity.ok("회원가입 성공");
    }
}