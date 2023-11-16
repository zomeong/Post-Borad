package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.security.UserDetailsImpl;
import com.sparta.post_board.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    //    @GetMapping("/feeds")
//    public List<FeedResponseDto> getAllFeeds(){
//        return feedService.getAllFeeds();
//    }
//
    @GetMapping("/feeds/{feedId}")
    public FeedResponseDto getFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return feedService.getFeed(feedId, userDetails.getUser());
    }
//
//    @PutMapping("/feeds/{id}")
//    public Feed updateFeed(@PathVariable Long id,@RequestBody FeedRequestDto requestDto){
//        return feedService.updateFeed(id, requestDto);
//    }
//
//    @DeleteMapping("/feeds/{id}")
//    public Long deleteFeed(@PathVariable Long id, @RequestBody FeedRequestDto requestDto){
//        return feedService.deleteFeed(id, requestDto);
//    }
}
