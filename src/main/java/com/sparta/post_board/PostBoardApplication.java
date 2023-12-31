package com.sparta.post_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PostBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostBoardApplication.class, args);
    }

}
