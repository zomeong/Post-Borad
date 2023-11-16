package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.CommentRequestDto;
import com.sparta.post_board.dto.CommentResponseDto;
import com.sparta.post_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/feeds")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{feedId}/comments")
    public CommentResponseDto createComment(@PathVariable Long feedId, @RequestBody CommentRequestDto requestDto){
        return commentService.createComment(feedId, requestDto);
    }

    @PutMapping(("/{feedId}/{commentId}"))
    public CommentResponseDto updateComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(feedId, commentId, requestDto);
    }

    @DeleteMapping("/{feedId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId,
                                                @RequestBody CommentRequestDto requestDto){
        commentService.deleteComment(feedId, commentId, requestDto);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
