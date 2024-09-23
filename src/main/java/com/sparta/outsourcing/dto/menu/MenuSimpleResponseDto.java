package com.sparta.outsourcing.dto.menu;

import com.sparta.outsourcing.entity.Menu;
import lombok.Getter;

@Getter
public class MenuSimpleResponseDto {
    private Long menuId;
    private String menuName;

    public MenuSimpleResponseDto(Menu menu) {
        this.menuId = menu.getId();
        this.menuName = menu.getMenuName();
    }
}
