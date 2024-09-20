package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.dto.review.ReviewSimpleResponseDto;
import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewResponseDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import com.sparta.outsourcing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<ReviewResponseDto> createReview (@RequestBody ReviewCreateRequestDto requestDto) {
        return ResponseEntity.ok(reviewService.createReview(requestDto));
    }

    @GetMapping("")
    public ResponseEntity<Page<ReviewSimpleResponseDto>> getReviews (@RequestParam int page,
                                                                     @RequestParam int size,
                                                                     @RequestParam Long storeId,
                                                                     @RequestParam(required = false) Integer minStar,
                                                                     @RequestParam(required = false) Integer maxStar) {
        return ResponseEntity.ok(reviewService.getReviews(page, size, storeId, minStar, maxStar));
    }

    @PutMapping("/{reviewId}/{customerId}")
    public ResponseEntity<ReviewResponseDto> updateReview (@PathVariable Long reviewId,@PathVariable Long customerId,  @RequestBody ReviewUpdateRequestDto requestDto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId,customerId, requestDto));
    }

    @DeleteMapping("/{reviewId}/{customerId}")
    public ResponseEntity deleteReview (@PathVariable Long reviewId,@PathVariable Long customerId) {
        reviewService.deleteReview(reviewId, customerId);
        return ResponseEntity.noContent().build();
    }


}
