package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByUserAndCompleteOrderByCreatedAtDesc(User user, boolean complete);
}
