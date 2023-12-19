package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feeds/{feedId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;

    @PostMapping("/comments")
    public CommentResponseDto createComment(@PathVariable Long feedId, @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentServiceImpl.createComment(feedId, requestDto, userDetails.getUser());
    }

    @PutMapping(("/comments/{commentId}"))
    public CommentResponseDto updateComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentServiceImpl.updateComment(feedId, commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentServiceImpl.deleteComment(feedId, commentId, userDetails.getUser());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
