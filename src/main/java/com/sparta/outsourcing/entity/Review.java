package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name="reviews")
public class Review extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int star;

    @Column(length = 255)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="store_id")
    private Store store;


    public Review(Customer customer, Store store, ReviewCreateRequestDto requestDto) {
        this.customer = customer;
        this.store = store;
        this.star = requestDto.getStar();
        this.content = requestDto.getContent();
    }

    public void update(ReviewUpdateRequestDto requestDto) {
        this.star = requestDto.getStar();
        this.content = requestDto.getContent();
    }
}
