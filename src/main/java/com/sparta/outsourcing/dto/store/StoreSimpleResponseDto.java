package com.sparta.outsourcing.dto.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.dto.menu.MenuSimpleResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreSimpleResponseDto {
    private Long storeId;
    private String storeName;
    private int minPrice;
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;

    public StoreSimpleResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.minPrice = store.getMinPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.address = store.getAddress();
    }


}
