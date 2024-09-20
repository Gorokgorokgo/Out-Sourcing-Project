package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.MenuRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "MENUS")
@NoArgsConstructor
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORES_ID", nullable = false)
    private Store store;

    public Menu(MenuRequestDto requestDto) {

        this.menuName = requestDto.getName();
        this.menuPrice = requestDto.getPrice();
//        this.store = requestDto.getStores
    }
}
