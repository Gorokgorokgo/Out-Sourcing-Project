package com.sparta.outsourcing.dto.review;

import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {
    private Long storeId;
    private int star;
    private  String content;
}
