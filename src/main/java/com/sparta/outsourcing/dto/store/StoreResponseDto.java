package com.sparta.outsourcing.dto.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.outsourcing.dto.file.FileResponseDto;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.entity.Image;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private int minPrice;
    private boolean storeStatus;
    private String address;
    private List<FileResponseDto> image;
    private List<MenuResponseDto> menus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;

    public StoreResponseDto() {
    }

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.minPrice = store.getMinPrice();
        this.storeStatus = store.isStoreStatus();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.address = store.getAddress();
        this.menus = store.getMenus().stream().map(menu -> new MenuResponseDto(menu)).toList();
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus.stream().map(menu -> new MenuResponseDto(menu)).toList();
    }

    public void setMenus2(List<MenuResponseDto> menus) {
        this.menus = menus;
    }



    public void setImage(List<Image> byItemIdAndImageEnum) {
        this.image = byItemIdAndImageEnum.stream().map(file -> new FileResponseDto(file)).toList();
    }
    @Override
    public String toString() {
        return "StoreResponseDto{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", minPrice=" + minPrice +
                ", storeStatus=" + storeStatus +
                ", address='" + address + '\'' +
                ", image=" + image +
                ", menus=" + menus +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                '}';
    }
}
