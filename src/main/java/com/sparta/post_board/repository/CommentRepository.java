package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndFeedId(Long commentId, Long feedId);
}
