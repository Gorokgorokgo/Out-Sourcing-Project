package com.sparta.outsourcing.dto.store;

import com.sparta.outsourcing.dto.menu.MenuSimpleResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

@Getter
public class StoreDetailResponseDto {
    private Long id;
    private String name;
    private MenuSimpleResponseDto menu;

    public StoreDetailResponseDto(Menu menu, Store store) {
        this.id = store.getStoreId();
        this.name = store.getStoreName();
        this.menu = new MenuSimpleResponseDto(menu);
    }
}
