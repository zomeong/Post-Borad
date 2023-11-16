package com.sparta.post_board.service;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.repository.CommentRepository;
import com.sparta.post_board.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;


    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto) {
        Feed feed = feedRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("선택한 피드가 존재하지 않습니다.")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, feed));
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long feedId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = findComment(commentId, feedId);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long feedId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = findComment(commentId, feedId);
        commentRepository.delete(comment);
    }

    private Comment findComment(Long commentId, Long feedId) {
        return commentRepository.findByIdAndFeedId(commentId, feedId).orElseThrow(()
                -> new IllegalArgumentException("선택한 댓글이 존재하지 않습니다.")
        );
    }
}