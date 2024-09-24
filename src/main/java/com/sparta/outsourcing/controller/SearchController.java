package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.search.SearchAllResponseDto;
import com.sparta.outsourcing.dto.search.SearchLatestResponseDto;
import com.sparta.outsourcing.dto.search.SearchRequestDto;
import com.sparta.outsourcing.dto.search.SearchResponsDto;
import com.sparta.outsourcing.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public SearchAllResponseDto searchAllUser (@RequestParam String keyword, @RequestParam String address) {
        return searchService.searchAll(keyword, address);
    }
    /**
     * 사용자가 지정한 주소를 중심으로 해당 키워드의 가게나 메뉴 찾기
     * @param keyword : 검색할 키워드
     * @param address : 사용자 지정한 주소
     * @return 검색결과 Dto
     */
    @GetMapping("/all/address")
    public SearchAllResponseDto searchAllAddress (@RequestParam String keyword, @RequestParam String address) {
        return searchService.searchAll(keyword, address);
    }


    @PostMapping("")
    public SearchResponsDto createSearch (@Auth AuthUser authUser, @RequestBody SearchRequestDto requestDto) {
        return searchService.createSearch(authUser, requestDto);

    }

    @GetMapping("/latest")
    public SearchLatestResponseDto searchLatest (@Auth AuthUser authUser) {
        return searchService.searchLatest(authUser);
    }

}
