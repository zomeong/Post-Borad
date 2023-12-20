package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedRepositoryQuery {
    Page<Feed> search(String keyword, Pageable pageable);
}
