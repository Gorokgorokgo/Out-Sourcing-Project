package com.sparta.outsourcing.dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.outsourcing.constant.MenuStatus;
import lombok.Getter;

@Getter
public class MenuUpdateDto {
    @JsonProperty(required = false)
    private String menuName;

    @JsonProperty(required = false)
    private int menuPrice;

    @JsonProperty(required = false)
    private MenuStatus menuStatus;
}
