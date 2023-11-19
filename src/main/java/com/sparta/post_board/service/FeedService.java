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
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        return new FeedResponseDto(feed);
    }

    public LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds() {
        List<User> userList = userRepository.findAll().stream().toList();
        List<FeedResponseDto> feedList = new ArrayList<>();
        LinkedHashMap<String, List<FeedResponseDto>> allFeedsList = new LinkedHashMap<>();

        for (User user : userList) {
            feedList = (List<FeedResponseDto>) feedRepository.findAllByUserAndCompleteOrderByCreatedAtDesc(user, false)
                    .stream().map(FeedResponseDto::new).toList();
            allFeedsList.put(user.getUsername(), feedList);
        }
        return allFeedsList;
    }

    public FeedResponseDto getFeed(Long id){
        Feed feed = findFeed(id);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public FeedResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.update(requestDto);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public void completeFeed(Long id, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.complete();
    }

    private Feed findFeed(Long id){
        return feedRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 피드는 존재하지 않습니다.")
        );
    }

    private void checkUser(Feed feed, User user){
        if(!feed.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("작성자만 수정/삭제 가능합니다.");
        }
    }
}
