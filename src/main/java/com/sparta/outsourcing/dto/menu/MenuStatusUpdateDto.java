package com.sparta.outsourcing.dto.menu;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.entity.Menu;
import lombok.Getter;

@Getter
public class MenuStatusUpdateDto {
    private MenuStatus menuStatus;

    public MenuStatusUpdateDto() {}

    public MenuStatusUpdateDto(Menu findMenu) {
        this.menuStatus = findMenu.getMenuStatus();
    }
}
