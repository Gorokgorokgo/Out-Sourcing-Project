package com.sparta.outsourcing.dto.review;

import com.sparta.outsourcing.entity.Image;
import com.sparta.outsourcing.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReviewSimpleResponseDto {
    private Long reviewId;
    private int star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Image> images = new ArrayList<>();


    public ReviewSimpleResponseDto(Review review, List<Image> images) {
        this.reviewId = review.getReviewId();
        this.star = review.getStar();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getUpdatedAt();
        this.images = images;
    }
}
