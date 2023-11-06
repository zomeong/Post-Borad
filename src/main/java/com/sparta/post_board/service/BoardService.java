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

    public Feed getFeed(Long id) {
        return findFeed(id);
    }

    private Feed findFeed(Long id){
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 피드는 존재하지 않습니다.")
        );
    }
}
