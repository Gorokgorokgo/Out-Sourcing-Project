package com.sparta.outsourcing.dto.store;

import com.sparta.outsourcing.dto.menu.MenuSimpleResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreDetailResponseDto {
    private Long storeId;
    private String storeName;
    private MenuSimpleResponseDto menu;

    public StoreDetailResponseDto(Menu menu, Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.menu = new MenuSimpleResponseDto(menu);
    }
}
