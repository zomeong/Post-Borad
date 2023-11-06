package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public FeedResponseDto createFeed(FeedRequestDto requestDto) {
        Feed feed = boardRepository.save(new Feed(requestDto));
        return new FeedResponseDto(feed);
    }

    public List<FeedResponseDto> getAllFeeds() {
        return boardRepository.findAllByOrderByModifiedAtDesc()
                .stream().map(FeedResponseDto::new).toList();
    }
}
