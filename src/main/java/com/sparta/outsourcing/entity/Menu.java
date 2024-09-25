package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import com.sparta.outsourcing.dto.menu.MenuUpdateDto;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_status", nullable = false)
    private MenuStatus menuStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;



    public Menu(Customer findCustomer, Store findStore, MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuStatus = MenuStatus.ACTIVE; // 기본 상태는 판매 중
        this.customer = findCustomer;
        this.store = findStore;
    }

    public void update(Customer findCustomer, Store findStore, MenuUpdateDto requestDto) {
        this.customer = findCustomer;
        this.store = findStore;

        // 메뉴 이름이 null이 아닌 경우에만 업데이트
        if (requestDto.getMenuName() != null) {
            this.menuName = requestDto.getMenuName();
        }

        // 메뉴 가격이 0보다 큰 경우에만 업데이트
        if (requestDto.getMenuPrice() > 0) {
            this.menuPrice = requestDto.getMenuPrice();
        }

        // 메뉴 상태가 null이 아닌 경우에만 업데이트
        if (requestDto.getMenuStatus() != null) {
            this.menuStatus = requestDto.getMenuStatus();
        }
    }

    public void setOutOfStock() {
        this.menuStatus = MenuStatus.OUT_OF_STOCK; // 품절로 상태 변경
    }

    public void markAsDeleted() {
        this.menuStatus = MenuStatus.DELETED; // 삭제로 상태 변경
    }

    public void setActive() {
        this.menuStatus = MenuStatus.ACTIVE; // 판매 중으로 상태 변경
    }
}
