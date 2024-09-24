package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.search.SearchRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name="searches")
public class Search extends Timestamped2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="search_id")
    private Long searchId;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")   //로그인 안하고 검색 가능(대신 최신 검색어를 쓸수 없음)
    private Customer customer;


    public Search(Customer customer, SearchRequestDto requestDto) {
        this.customer = customer;
        this.keyword = requestDto.getKeyword();
    }
}
