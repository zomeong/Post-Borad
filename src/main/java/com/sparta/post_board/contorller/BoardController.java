package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @PostMapping("/feeds")
    public FeedResponseDto createFeed(@RequestBody FeedRequestDto requestDto){
        return boardService.createFeed(requestDto);
    }

    @GetMapping("/feeds")
    public List<FeedResponseDto> getAllFeeds(){
        return boardService.getAllFeeds();
    }

    @GetMapping("/feeds/{id}")
    public Feed getFeed(@PathVariable Long id){
        return boardService.getFeed(id);
    }

    @PutMapping("/feeds/{id}")
    public Feed updateFeed(@PathVariable Long id,@RequestBody FeedRequestDto requestDto){
        return boardService.updateFeed(id, requestDto);
    }

    @DeleteMapping("/feeds/{id}")
    public Long deleteFeed(@PathVariable Long id, @RequestBody FeedRequestDto requestDto){
        return boardService.deleteFeed(id, requestDto);
    }
}
