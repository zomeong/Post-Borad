package com.sparta.post_board.dto;

import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public FeedResponseDto(Feed feed, User user) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.username = user.getUsername();
        this.contents = feed.getContents();
        this.createdAt = feed.getCreatedAt();
        this.modifiedAt = feed.getModifiedAt();
    }
}
