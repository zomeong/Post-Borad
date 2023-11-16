package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;

    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user) {
        // 새 피드 저장
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        return new FeedResponseDto(feed, user);
    }

//    public List<FeedResponseDto> getAllFeeds() {
//        // 모든 피드 목록 조회
//        return feedRepository.findAllByOrderByModifiedAtDesc()
//                .stream().map(FeedResponseDto::new).toList();
//    }

    public Feed getFeed(Long id){
        // 선택 피드 조회
        return feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 피드는 존재하지 않습니다.")
        );
    }

//    @Transactional
//    public Feed updateFeed(Long id, FeedRequestDto requestDto) {
//        // 선택 피드 수정
////        Feed feed = checkPassword(id, requestDto.getPassword());
////        feed.update(requestDto);
// //       return feed;
//    }
//
//    @Transactional
//    public Long deleteFeed(Long id, FeedRequestDto requestDto) {
//        // 선택 포스트 삭제
//  //      Feed feed = checkPassword(id, requestDto.getPassword());
// //       feedRepository.delete(feed);
//        return id;
//    }
}
