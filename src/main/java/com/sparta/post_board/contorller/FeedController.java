package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/feeds")
    public FeedResponseDto createFeed(@RequestBody FeedRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return feedService.createFeed(requestDto, userDetails.getUser());
    }

    @GetMapping("/feeds")
    public LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return feedService.getAllFeeds(userDetails.getUser());
    }

    @GetMapping("/feeds/{id}")
    public FeedResponseDto getFeed(@PathVariable Long id) {
        return feedService.getFeed(id);
    }

    @PutMapping("/feeds/{id}")
    public FeedResponseDto updateFeed(@PathVariable Long id, @RequestBody FeedRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return feedService.updateFeed(id, requestDto, userDetails.getUser());
    }

    @PutMapping("/feeds/{id}/complete")
    public ResponseEntity<String> completeFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedService.completeFeed(id, userDetails.getUser());
        return ResponseEntity.ok("할일 완료!");
    }

    @PutMapping("/feeds/{id}/blind")
    public ResponseEntity<String> blindFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedService.blindFeed(id, userDetails.getUser());
        return ResponseEntity.ok("할일이 비공개 처리 되었습니다.");
    }
}
