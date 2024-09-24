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

    /**
     * 사용자 주소를 중심으로 해당 키워드의 가게나 메뉴 찾기
     * @param keyword : 검색할 키워드
     * @param address : 사용자 주소(나중에 AuthUser로 바뀌어야함)
     * @return 검색결과 Dto
     */
    @GetMapping("/all/user")
    public SearchResponseDto searchAllUser (@RequestParam String keyword, @RequestParam String address) {
        return searchService.searchAll(keyword, address);
    }
    /**
     * 사용자가 지정한 주소를 중심으로 해당 키워드의 가게나 메뉴 찾기
     * @param keyword : 검색할 키워드
     * @param address : 사용자 지정한 주소
     * @return 검색결과 Dto
     */
    @GetMapping("/all/address")
    public SearchResponseDto searchAllAddress (@RequestParam String keyword, @RequestParam String address) {
        return searchService.searchAll(keyword, address);
    }


}
