package com.sparta.outsourcing.dto.store;

import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponseDto {
    private Long id;
    private String name;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
    }
}
