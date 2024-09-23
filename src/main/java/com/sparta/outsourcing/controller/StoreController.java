package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.dto.store.StoreStatusUpdateDto;
import com.sparta.outsourcing.dto.store.StoreUpdateRequestDto;
import com.sparta.outsourcing.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    // 가게 생성
    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> createStore(@Auth AuthUser authUser, @RequestBody StoreRequestDto requestDto) {
        return ResponseEntity.ok(storeService.createStore(authUser, requestDto));
    }

    // 가게 단건 조회 + 메뉴 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    // 가게 다건 조회
    @GetMapping("/stores")
    public ResponseEntity<Page<StoreResponseDto>> getStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.getStores(pageable));
    }

    // 가게 수정
    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@Auth AuthUser authUser,
                                                        @PathVariable Long storeId,
                                                        @RequestBody StoreUpdateRequestDto requestDto) {
        return ResponseEntity.ok(storeService.updateStore(authUser, storeId, requestDto));
    }

    // 가게 상태 변경
    @PatchMapping("/stores/{storeId}/status")
    public ResponseEntity<StoreStatusUpdateDto> updateStoreStatus(@Auth AuthUser authUser,
                                                                  @PathVariable Long storeId,
                                                                  @RequestBody StoreStatusUpdateDto statusUpdateDto) {
        return ResponseEntity.ok(storeService.updateStoreStatus(authUser, storeId, statusUpdateDto.isStoreStatus()));
    }
}
