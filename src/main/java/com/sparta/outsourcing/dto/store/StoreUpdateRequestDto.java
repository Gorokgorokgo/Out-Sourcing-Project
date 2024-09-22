package com.sparta.outsourcing.dto.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreUpdateRequestDto {
    @JsonProperty(required = false)
    private String storeName;

    @JsonProperty(required = false)
    private int minPrice;

    @JsonProperty(required = false)
    private LocalTime openTime;

    @JsonProperty(required = false)
    private LocalTime closeTime;

}
