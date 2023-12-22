package com.sparta.post_board.service;

import com.sparta.post_board.common.PageDto;
import com.sparta.post_board.common.S3Uploader;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
import com.sparta.post_board.repository.FeedRepository;
import com.sparta.post_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user, MultipartFile image) {
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());

        if(fileExtension != null){
            String imageUrl = s3Uploader.uploadImage(feed.getId(), image);
            feed.setImageUrl(imageUrl);
        }

        return new FeedResponseDto(feed);
    }

    @Override
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

    @Override
    public FeedResponseDto getFeed(Long id) {
        Feed feed = findFeed(id);
        return new FeedResponseDto(feed);
    }

    @Override
    @Transactional
    public FeedResponseDto updateFeed(Long id, FeedRequestDto requestDto, MultipartFile image, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.update(requestDto);

        String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        if(fileExtension != null){
            s3Uploader.deleteImage(image.getName());
            String imageUrl = s3Uploader.uploadImage(feed.getId(), image);
            feed.setImageUrl(imageUrl);
        }

        return new FeedResponseDto(feed);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> searchFeed(String keyword, PageDto pageDto){
        Page<Feed> feeds = feedRepository.search(keyword, pageDto.toPageable());

        if(feeds.isEmpty()){
            throw new NotFoundException("검색 결과");
        }

        List<FeedResponseDto> feedResponseDtos = feeds.getContent().stream()
                .map(FeedResponseDto::new)
                .toList();
        return new PageImpl<>(feedResponseDtos, pageDto.toPageable(), feeds.getTotalElements());
    }

    @Override
    @Transactional
    public void completeFeed(Long id, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.complete();
    }

    @Override
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
