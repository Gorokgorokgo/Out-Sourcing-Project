package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.constant.Status;
import com.sparta.outsourcing.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "STORES")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private int minPrice;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customers_id")
//    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Store(StoreRequestDto requestDto) {
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minPrice = requestDto.getMinPrice();
        this.status = requestDto.getStatus();
    }

    public void update(StoreRequestDto requestDto) {
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.minPrice = requestDto.getMinPrice();
        this.status = requestDto.getStatus();
    }
}
