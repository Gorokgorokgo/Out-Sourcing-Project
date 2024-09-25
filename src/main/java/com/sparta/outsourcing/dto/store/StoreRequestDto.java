package com.sparta.outsourcing.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequestDto {
    private String storeName;
    private int minPrice;
    private boolean storeStatus;
    private String address;
    private LocalTime openTime;
    private LocalTime closeTime;

}
