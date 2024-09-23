package com.sparta.outsourcing.dto.store;

import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreStatusUpdateDto {
    private boolean storeStatus;

    public StoreStatusUpdateDto() {}

    public StoreStatusUpdateDto(Store findStore) {
        this.storeStatus = findStore.isStoreStatus();
    }
}
