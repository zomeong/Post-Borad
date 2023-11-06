package com.sparta.post_board.dto;

import com.sparta.post_board.entity.Feed;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public FeedResponseDto(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.username = feed.getUsername();
        this.contents = feed.getContents();
//        this.createDate = feed.getCreateAt();
//        this.modifiedDate = feed.getModifiedAt();
    }
}
