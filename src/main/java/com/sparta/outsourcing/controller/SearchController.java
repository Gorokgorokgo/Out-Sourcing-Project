package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

//    @GetMapping("/all")
//    public List<StoreResponseDto> searchAll (@RequestParam String keyword) {
//        return searchService.searchAll(keyword);
//    }
}
