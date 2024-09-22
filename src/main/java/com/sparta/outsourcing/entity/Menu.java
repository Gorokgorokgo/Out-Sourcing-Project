package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(MenuRequestDto requestDto, Store store) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.store = store;
    }

    public void update(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
    }
}
