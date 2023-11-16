package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user) {
        // 새 피드 저장
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        return new FeedResponseDto(feed);
    }

    public LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds() {
        // 모든 피드 목록 조회
        List<User> userList = userRepository.findAll().stream().toList();   // user 리스트 조회
        List<FeedResponseDto> feedList = new ArrayList<>();
        LinkedHashMap<String, List<FeedResponseDto>> allFeedsList = new LinkedHashMap<>();

        for (User user : userList) {
            // user의 feedlist 찾아오기
            feedList = (List<FeedResponseDto>) feedRepository.findAllByUserOrderByCreatedAtDesc(user)
                    .stream().map(FeedResponseDto::new).toList();

            // username을 key, feedList를 value로 map에 저장
            allFeedsList.put(user.getUsername(), feedList);
        }

        return allFeedsList;
    }

    public FeedResponseDto getFeed(Long id){
        // 선택 피드 조회
        Feed feed = findFeed(id);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public FeedResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user) {
        // 선택 피드 수정
        Feed feed = findFeed(id);

        if(!feed.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("본인이 작성한 피드만 수정 가능합니다.");
        }

        feed.update(requestDto);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public void completeFeed(Long id, User user) {
        // 선택한 피드 완료 처리
        Feed feed = findFeed(id);

        if(!feed.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("본인이 작성한 피드만 완료 가능합니다.");
        }

        feed.complete();
    }

    private Feed findFeed(Long id){
        return feedRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 피드는 존재하지 않습니다.")
        );
    }
}
