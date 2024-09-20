package com.sparta.outsourcing.dto;

import com.sparta.outsourcing.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private Long storeId;
    private Long menuId;
    private String name;
    private int price;

    public MenuResponseDto(Menu menu) {
        this.storeId = menu.getStore().getStoreId();
        this.menuId = menu.getMenuId();
        this.name = menu.getMenuName();
        this.price = menu.getMenuPrice();
    }
}
