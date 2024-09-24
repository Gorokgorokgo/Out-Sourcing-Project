package com.sparta.outsourcing.dto.store;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreRequestDto {
    private String storeName;
    private int minPrice;
    private boolean storeStatus;
    private String address;
    private LocalTime openTime;
    private LocalTime closeTime;

}
