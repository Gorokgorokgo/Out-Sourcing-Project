package com.sparta.outsourcing.dto.review;

import com.sparta.outsourcing.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewSimpleResponseDto {
    private Long reviewId;
    private int star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReviewSimpleResponseDto(Review review) {
        this.reviewId = review.getId();
        this.star = review.getStar();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }
}
