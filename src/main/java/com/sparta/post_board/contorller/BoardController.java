package com.sparta.post_board.contorller;

import com.sparta.post_board.dto.FeedRequestDto;
import com.sparta.post_board.dto.FeedResponseDto;
import com.sparta.post_board.service.BoardService;
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

//    @GetMapping("/feeds")
//    public List<FeedResponseDto> getAllFeeds(){
//        return boardService.getAllFeeds();
//    }
//
//    @GetMapping("/feeds/{id}")
//    public Long getFeed(@PathVariable Long id){
//        return boardService.getFeed();
//    }
//
//    @PutMapping("/feeds/{id}")
//    public Long updateFeed(@PathVariable Long id, @RequestBody FeedRequestDto requestDto){
//        return boardService.updateFeed(id, requestDto);
//    }
//
//    @DeleteMapping("/feed/{id}")
//    public Long deleteFeed(@PathVariable Long id){
//        return boardService.deleteFeed(id);
//    }
}
