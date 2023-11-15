package com.sparta.post_board.entity;

import com.sparta.post_board.dto.FeedRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "feed")
@NoArgsConstructor
public class Feed extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "feed")
    private List<Comment> commentList = new ArrayList<>();

    public Feed(FeedRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void update(FeedRequestDto requestDto) {
        // 제목, 내용만 수정 가능
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
