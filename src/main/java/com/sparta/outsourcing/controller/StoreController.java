package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.dto.StoreRequestDto;
import com.sparta.outsourcing.dto.StoreResponseDto;
import com.sparta.outsourcing.sevice.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> create(@RequestBody StoreRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.create(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        return new ResponseEntity<>(storeService.getStore(storeId), HttpStatus.OK);
    }

    @GetMapping("/stores")
    public ResponseEntity<Page<StoreResponseDto>> getStores(Pageable pageable) {
        Page<StoreResponseDto> responseDtoPage = storeService.getStores(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> update(@PathVariable Long storeId, @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.update(storeId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<Void> delete(@PathVariable Long storeId) {
        storeService.delete(storeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
