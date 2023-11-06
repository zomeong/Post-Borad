package com.sparta.post_board.repository;

import com.sparta.post_board.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardReposiitory extends JpaRepository<Feed, Long> {
}
