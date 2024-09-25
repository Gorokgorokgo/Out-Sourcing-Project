package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewResponseDto;
import com.sparta.outsourcing.dto.review.ReviewSimpleResponseDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import com.sparta.outsourcing.exception.customer.DifferentUsersException;
import com.sparta.outsourcing.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
//@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("")

    public ResponseEntity<ReviewResponseDto> createReview (@Auth AuthUser authUser,
                                                           @RequestPart ReviewCreateRequestDto requestDto,
                                                           @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(reviewService.createReview(authUser.getCustomerId(), requestDto, files));
    }

    @GetMapping("")
    public ResponseEntity<Page<ReviewSimpleResponseDto>> getReviews (@RequestParam(defaultValue = "1", required = false) int page,
                                                                     @RequestParam(defaultValue = "10", required = false) int size,
                                                                     @RequestParam Long storeId,
                                                                     @RequestParam(defaultValue = "1",required = false) int minStar,
                                                                     @RequestParam(defaultValue = "5",required = false) int maxStar) {
        return ResponseEntity.ok(reviewService.getReviews(page, size, storeId, minStar, maxStar));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview (@PathVariable Long reviewId,
                                                           @Auth AuthUser authUser,
                                                           @RequestPart(required = false) ReviewUpdateRequestDto requestDto,
                                                           @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException, DifferentUsersException {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, authUser.getCustomerId(), requestDto, files));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview (@Auth AuthUser authUser,@PathVariable Long reviewId) {
        System.out.println("reviewId = " + reviewId);
        reviewService.deleteReview(reviewId, authUser.getCustomerId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}/images/{imageId}")
    public ResponseEntity<ReviewResponseDto> deleteReviewImages (@Auth AuthUser authUser,
                                              @PathVariable Long reviewId,
                                              @PathVariable Long imageId) {

        return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReviewImages(reviewId, imageId, authUser.getCustomerId()));
    }

}
