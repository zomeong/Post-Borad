package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.SignupRequestDto;
import com.sparta.post_board.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지가 없어 get mapping은 구현하지 않음
    @PostMapping("/user/signup")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        // bindingResult에 에러가 담김
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if(!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/board/user/signup";
        }

        userService.signup(requestDto);

        return "redirect:/board/user/login";
    }
}