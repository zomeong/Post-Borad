package com.sparta.post_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "사용자 이름은 필수값 입니다.")
    @Pattern(regexp = "^[a-z0-9]+$")
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @Size(min = 8, max = 15, message = "비밀번호 8자 이상 15자 이하여야 합니다.")
    private String password;
}
