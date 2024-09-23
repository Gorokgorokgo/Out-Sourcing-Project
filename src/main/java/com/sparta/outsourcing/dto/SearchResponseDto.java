package com.sparta.outsourcing.dto;

import com.sparta.outsourcing.dto.store.StoreDetailResponseDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResponseDto {
    private List<StoreResponseDto> storeList;
    private List<StoreDetailResponseDto> storeMenuList;

    public SearchResponseDto(List<StoreResponseDto> storeResult, List<StoreDetailResponseDto> menuResult) {
        this.storeList = storeResult;
        this.storeMenuList = menuResult;
    }
}
