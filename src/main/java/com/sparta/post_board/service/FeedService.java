package com.sparta.post_board.service;

import com.sparta.post_board.common.PageDto;
import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;

public interface FeedService {
    /**
     * 게시글 생성
     * @param requestDto 게시글 생성 요청정보
     * @param user 게시글 생성 요청자
     * @param image 게시글에 첨부할 이미지
     * @return 게시글 생성 결과
     */
    FeedResponseDto createFeed(FeedRequestDto requestDto, User user, MultipartFile image);

    /**
     * 모든 게시글 조회
     * @param loginUser 현재 로그인한 유저
     * @return 게시글 조회 결과
     */
    LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds(User loginUser);

    /**
     * 선택 게시글 조회
     * @param id 게시글 id
     * @return 게시글 조회 결과
     */
    FeedResponseDto getFeed(Long id);

    /**
     * 게시글 검색
     * @param keyword 검색 키워드
     * @param pageDto 페이지 정보
     * @return 게시글 검색 결과
     */
    Page<FeedResponseDto> searchFeed(String keyword, PageDto pageDto);

    /**
     * 게시글 수정
     * @param id 게시글 id
     * @param requestDto 게시글 수정 요청 정보
     * @param user 게시글 수정 요청자
     * @return 게시글 수정 결과
     */
    FeedResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user);

    /**
     * 게시글 완료
     * @param id 게시글 id
     * @param user 게시글 완료 요청자
     */
    void completeFeed(Long id, User user);

    /**
     * 게시글 비공개
     * @param id 게시글 id
     * @param user 게시글 비공개 요청자
     */
    void blindFeed(Long id, User user);
}
