package com.sparta.outsourcing.dto;

import com.sparta.outsourcing.constant.Status;
import lombok.Getter;

@Getter
public class StoreRequestDto {
    private String openTime;
    private String closeTime;
    private int minPrice;

    private Status status;
}
