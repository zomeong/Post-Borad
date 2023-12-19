package com.sparta.post_board.service;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.entity.User;

public interface CommentService {

    /**
     * 댓글 생성
     * @param id 게시글 id
     * @param requestDto 댓글 생성 정보
     * @param user 댓글 생성 요청자
     * @return 댓글 생성 결과
     */
    CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user);

    /**
     * 댓글 수정
     * @param feedId 게시글 id
     * @param commentId 댓글 id
     * @param requestDto 댓글 수정 정보
     * @param user 댓글 수정 요청자
     * @return 댓글 수정 결과
     */
    CommentResponseDto updateComment(Long feedId, Long commentId, CommentRequestDto requestDto, User user);

    /**
     * 댓글 삭제
     * @param feedId 게시글 id
     * @param commentId 댓글 id
     * @param user 댓글 삭제 요청자
     */
    void deleteComment(Long feedId, Long commentId, User user);
}
