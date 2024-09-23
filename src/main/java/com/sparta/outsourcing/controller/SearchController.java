package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.dto.SearchResponseDto;
import com.sparta.outsourcing.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/all")
    public SearchResponseDto searchAll (@RequestParam String keyword, @RequestParam String address) {
        return searchService.searchAll(keyword, address);
    }
}
