package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name="reivews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int star;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="store_id")
    private Store store;

    @CreatedDate
    @Column(updatable = false) // 업데이트 안되게 만들어야 함.
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // createdAt 만든 시각.

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP) // 데이터 타입 맞추기
    private LocalDateTime modifiedAt; // 조회한 entity 값이 변경 될 때마다, 해당 변경시간으로 변경됨.


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
