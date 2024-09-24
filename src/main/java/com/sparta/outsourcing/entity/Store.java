package com.sparta.outsourcing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(name = "store_name", nullable = false, length = 200)
    private String storeName;

    @Column(nullable = false)
    private int minPrice;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean storeStatus;    // true: 개업, false: 폐업

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @Column(name = "address", nullable = false)
    private String address;

    public Store(Customer customer, StoreRequestDto requestDto) {
        this.customer = customer;
        this.storeName = requestDto.getStoreName();
        this.minPrice = requestDto.getMinPrice();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.storeStatus = requestDto.isStoreStatus();
        this.address = requestDto.getAddress();
        this.menus = new ArrayList<>(); // menus 초기화
    }

    public void update(Customer customer, StoreUpdateRequestDto requestDto) {
        this.customer = customer;
        if (requestDto.getStoreName() != null) this.storeName = requestDto.getStoreName();
        if (requestDto.getOpenTime() != null) this.openTime = requestDto.getOpenTime();
        if (requestDto.getCloseTime() != null) this.closeTime = requestDto.getCloseTime();
        if (requestDto.getAddress() != null) this.address = requestDto.getAddress();
            if (requestDto.getMinPrice() > 0) this.minPrice = requestDto.getMinPrice();
        // 최소 가격이 0보다 큰 경우만 업데이트
    }

    public void closeStore() {
        this.storeStatus = false;
    }

    public void openStore() {
        this.storeStatus = true;
    }
}
