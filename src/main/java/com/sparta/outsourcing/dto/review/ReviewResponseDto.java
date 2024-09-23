package com.sparta.outsourcing.dto.review;

import com.sparta.outsourcing.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private Long customerId;
    private Long storeId;
    private int star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReviewResponseDto(Review saveReview) {
        this.reviewId = saveReview.getId();
        this.customerId = saveReview.getCustomer().getCustomerId();
        this.storeId = saveReview.getStore().getStoreId();
        this.star = saveReview.getStar();
        this.content = saveReview.getContent();
        this.createdAt = saveReview.getCreatedAt();
        this.modifiedAt = saveReview.getUpdatedAt();
    }
}
