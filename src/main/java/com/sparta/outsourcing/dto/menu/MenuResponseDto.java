package com.sparta.outsourcing.dto.menu;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.entity.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
//@Builder
public class MenuResponseDto {
    private Long customerId;
    private Long storeId;
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private MenuStatus menuStatus;

    public MenuResponseDto(Menu menu) {
        this.customerId = menu.getCustomer().getCustomerId();
        this.storeId = menu.getStore().getStoreId();
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuStatus = menu.getMenuStatus();
    }

//    public static MenuResponseDto ofDto(Menu menu) {
//        return MenuResponseDto.builder()
//                .storeId(menu.getStore().getStoreId())
//                .menuId(menu.getMenuId())
//                .menuName(menu.getMenuName())
//                .menuPrice(menu.getMenuPrice())
//                .build();
//    }
}
