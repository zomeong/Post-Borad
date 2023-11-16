package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/feeds")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{feedId}/comments")
    public CommentResponseDto createComment(@PathVariable Long feedId, @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(feedId, requestDto, userDetails.getUser());
    }

    @PutMapping(("/{feedId}/{commentId}"))
    public CommentResponseDto updateComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(feedId, commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{feedId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(feedId, commentId, userDetails.getUser());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
