package com.sparta.outsourcing.dto.menu;

import com.sparta.outsourcing.constant.MenuStatus;
import lombok.Getter;

@Getter
public class MenuRequestDto {
    private String menuName;
    private int menuPrice;
    private MenuStatus menuStatus;
}
