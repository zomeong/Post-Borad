package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByModifiedAtDesc();

}
