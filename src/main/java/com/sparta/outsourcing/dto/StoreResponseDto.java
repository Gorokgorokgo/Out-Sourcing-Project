package com.sparta.outsourcing.dto;

import com.sparta.outsourcing.constant.Status;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponseDto {

    private Long storeId;
    private String openTime;
    private String closeTime;
    private int minPrice;

    private Status status;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.minPrice = store.getMinPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.status = store.getStatus();
    }
}
