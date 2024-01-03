package com.sparta.post_board.service;

import com.sparta.post_board.dto.UserRequestDto;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.entity.UserRoleEnum;
import com.sparta.post_board.exception.DuplicateUserException;
import com.sparta.post_board.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateUserException();
        }

        // 사용자 등록
        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role);
        userRepository.save(user);
    }
}
