package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public FeedResponseDto createFeed(FeedRequestDto requestDto) {
        // 새 피드 저장
        Feed feed = boardRepository.save(new Feed(requestDto));
        return new FeedResponseDto(feed);
    }

    public List<FeedResponseDto> getAllFeeds() {
        // 모든 피드 목록 조회
        return boardRepository.findAllByOrderByModifiedAtDesc()
                .stream().map(FeedResponseDto::new).toList();
    }

    public Feed getFeed(Long id){
        // 선택 피드 조회
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 피드는 존재하지 않습니다.")
        );
    }

    @Transactional
    public Feed updateFeed(Long id, FeedRequestDto requestDto) {
        // 선택 피드 수정
        Feed feed = checkPassword(id, requestDto.getPassword());
        feed.update(requestDto);
        return feed;
    }

    @Transactional
    public Long deleteFeed(Long id, FeedRequestDto requestDto) {
        // 선택 포스트 삭제
        Feed feed = checkPassword(id, requestDto.getPassword());
        boardRepository.delete(feed);
        return id;
    }

    private Feed checkPassword(Long id, String getPassword) {
        // 비밀번호 확인
        Feed feed = getFeed(id);
        if (!getPassword.equals(feed.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return feed;
    }
}
