package com.sparta.outsourcing.dto.menu;

import lombok.Getter;

@Getter
public class MenuRequestDto {

    private Long storeId;
    private String menuName;
    private int menuPrice;

}
