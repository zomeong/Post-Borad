package com.sparta.post_board.service;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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

    public LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds(User loginUser) {
        List<User> userList = userRepository.findAll().stream().toList();
        List<FeedResponseDto> feedList;
        LinkedHashMap<String, List<FeedResponseDto>> allFeedsList = new LinkedHashMap<>();

        for (User user : userList) {
            feedList = feedRepository.findAllByUserAndCompleteOrderByCreatedAtDesc(user, false)
                    .stream()
                    .filter(feed -> !feed.isBlind() || user.getId().equals(loginUser.getId()))
                    .map(FeedResponseDto::new).toList();
            allFeedsList.put(user.getUsername(), feedList);
        }
        return allFeedsList;
    }

    public List<FeedResponseDto> searchFeed(String keyword) {
        List<FeedResponseDto> feedList = feedRepository.findByTitleOrderByCreatedAtDesc(keyword)
                .stream()
                .filter(Objects::nonNull)
                .map(FeedResponseDto::new).toList();

        if(feedList.isEmpty()){
            throw new NotFoundException("검색 결과");
        }
        return feedList;
    }

    public FeedResponseDto getFeed(Long id) {
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

    @Transactional
    public void blindFeed(Long id, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.blind();
    }

    private Feed findFeed(Long id) {
        return feedRepository.findById(id).orElseThrow(() ->
                new NotFoundException("피드")
        );
    }

    private void checkUser(Feed feed, User user) {
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new OnlyAuthorAccessException();
        }
    }
}
