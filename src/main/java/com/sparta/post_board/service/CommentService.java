package com.sparta.post_board.service;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.entity.Comment;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.User;
import com.sparta.post_board.exception.NotFoundException;
import com.sparta.post_board.exception.OnlyAuthorAccessException;
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

    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        Feed feed = feedRepository.findById(id).orElseThrow(()
                -> new NotFoundException("피드")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, feed, user));
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long feedId, Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId, feedId);
        checkUser(comment, user);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long feedId, Long commentId, User user) {
        Comment comment = findComment(commentId, feedId);
        checkUser(comment, user);
        commentRepository.delete(comment);
    }

    private Comment findComment(Long commentId, Long feedId) {
        return commentRepository.findByIdAndFeedId(commentId, feedId).orElseThrow(()
                -> new NotFoundException("댓글")
        );
    }

    private void checkUser(Comment comment, User user){
        if(!comment.getUser().getId().equals(user.getId())){
            throw new OnlyAuthorAccessException();
        }
    }
}
