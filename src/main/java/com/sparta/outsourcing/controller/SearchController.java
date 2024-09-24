package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.search.SearchAllResponseDto;
import com.sparta.outsourcing.dto.search.SearchLatestResponseDto;
import com.sparta.outsourcing.dto.search.SearchRequestDto;
import com.sparta.outsourcing.dto.search.SearchResponsDto;
import com.sparta.outsourcing.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;


    /**
     * 사용자가 지정한 주소 또는 로그인 정보의 주소를 중심으로 해당 키워드의 가게나 메뉴 찾기
     * @param keyword : 검색할 키워드
     * @param address : 사용자 지정한 주소
     * @return 검색결과 Dto
     */
    @GetMapping("/all/address")
    public ResponseEntity<SearchAllResponseDto> searchAllAddress (@RequestParam String keyword, @RequestParam String address) {
        return ResponseEntity.ok(searchService.searchAll(keyword, address));
    }


    @PostMapping("")
    public ResponseEntity<SearchResponsDto> createSearch (@Auth AuthUser authUser, @RequestBody SearchRequestDto requestDto) {
        return ResponseEntity.ok(searchService.createSearch(authUser, requestDto));

    }

    @GetMapping("/latest")
    public ResponseEntity<SearchLatestResponseDto> searchLatest (@Auth AuthUser authUser) {
        return ResponseEntity.ok(searchService.searchLatest(authUser));
    }

}
