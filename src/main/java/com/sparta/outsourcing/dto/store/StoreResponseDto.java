package com.sparta.outsourcing.dto.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private int minPrice;
    private boolean storeStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.minPrice = store.getMinPrice();
        this.storeStatus = store.isStoreStatus();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
    }

}
