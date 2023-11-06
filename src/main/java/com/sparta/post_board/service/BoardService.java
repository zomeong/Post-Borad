package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public FeedResponseDto createFeed(FeedRequestDto requestDto) {
        Feed feed = new Feed(requestDto);
        Feed saveFeed = boardRepository.save(feed);
        return new FeedResponseDto(saveFeed);
    }

}
