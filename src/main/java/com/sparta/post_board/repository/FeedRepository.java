package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedAtDesc();    // 작성일 기준 내림차순 정렬

    Optional<Feed> findByIdAndUserId(Long id, Long userId);
}
