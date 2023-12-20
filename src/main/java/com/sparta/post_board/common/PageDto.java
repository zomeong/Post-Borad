package com.sparta.post_board.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Builder
@AllArgsConstructor
public class PageDto {
    private final Integer currentPage;
    private final Integer size;
    private String sortBy;

    public Pageable toPageable() {
        if(Objects.isNull(sortBy)){
            return PageRequest.of(currentPage-1, size);
        }
        return PageRequest.of(currentPage-1, size, Sort.by(sortBy).descending());
    }
}