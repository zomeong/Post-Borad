package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.service.FeedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedServiceImpl feedServiceImpl;

    @PostMapping
    public FeedResponseDto createFeed(@RequestBody FeedRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return feedServiceImpl.createFeed(requestDto, userDetails.getUser());
    }

    @GetMapping
    public LinkedHashMap<String, List<FeedResponseDto>> getAllFeeds(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return feedServiceImpl.getAllFeeds(userDetails.getUser());
    }

    @GetMapping("/search")
    public List<FeedResponseDto> searchFeed(@RequestParam String keyword){
        return feedServiceImpl.searchFeed(keyword);
    }

    @GetMapping("/{feedId}")
    public FeedResponseDto getFeed(@PathVariable Long feedId) {
        return feedServiceImpl.getFeed(feedId);
    }

    @PutMapping("/{feedId}")
    public FeedResponseDto updateFeed(@PathVariable Long feedId, @RequestBody FeedRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return feedServiceImpl.updateFeed(feedId, requestDto, userDetails.getUser());
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedServiceImpl.completeFeed(id, userDetails.getUser());
        return ResponseEntity.ok("할일 완료!");
    }

    @PutMapping("/{id}/blind")
    public ResponseEntity<String> blindFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedServiceImpl.blindFeed(id, userDetails.getUser());
        return ResponseEntity.ok("할일이 비공개 처리 되었습니다.");
    }
}
