package com.sparta.outsourcing.dto.store;

import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private Long storeId;
    private String storeName;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
    }
}
