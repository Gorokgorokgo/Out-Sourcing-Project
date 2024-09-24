package com.sparta.outsourcing.dto.search;

import com.sparta.outsourcing.dto.store.StoreDetailResponseDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchAllResponseDto {
    private List<StoreResponseDto> storeList;
    private List<StoreDetailResponseDto> storeMenuList;

    public SearchAllResponseDto(List<StoreResponseDto> storeResult, List<StoreDetailResponseDto> menuResult) {
        this.storeList = storeResult;
        this.storeMenuList = menuResult;
    }
}
