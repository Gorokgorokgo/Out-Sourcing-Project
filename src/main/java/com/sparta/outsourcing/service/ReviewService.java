package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewResponseDto;
import com.sparta.outsourcing.dto.review.ReviewSimpleResponseDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Review;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.ReviewRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequestDto requestDto) {
        Customer customer = customerRepository.findById(requestDto.getCustomerId()).orElseThrow(()-> new NullPointerException("해당하는 고객이 존재하지 않습니다."));
        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(()->new NullPointerException("해당하는 매장이 존재하지 않습니다."));
        Review review = new Review(customer, store, requestDto);
        Review saveReview = reviewRepository.save(review);
        return new ReviewResponseDto(saveReview);
    }

    public Page<ReviewSimpleResponseDto> getReviews(int page, int size, Long storeId, Integer minStar, Integer maxStar) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("modifiedAt").descending());
        if(minStar == null) minStar = 0;
        if(maxStar == null) maxStar = 5;
        Store store = storeRepository.findById(storeId).orElseThrow(()->new NullPointerException("해당하는 매장이 존재하지 않습니다."));
        Page<Review> reviewList = reviewRepository.findAllByStoreAndStarBetween(store, minStar, maxStar, pageable);
        return reviewList.map(ReviewSimpleResponseDto::new);
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, Long customerId, ReviewUpdateRequestDto requestDto) {
        customerRepository.findById(customerId).orElseThrow(()-> new NullPointerException("해당 고객이 존재하지 않습니다."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new NullPointerException("해당 댓글이 존재하지 않습니다."));
        if(!customerId.equals(review.getCustomer().getId())) throw new IllegalArgumentException("작성자가 아니므로 수정이 불가능합니다.");
        review.update(requestDto);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long customerId) {
        customerRepository.findById(customerId).orElseThrow(()-> new NullPointerException("해당 고객이 존재하지 않습니다."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new NullPointerException("해당 댓글이 존재하지 않습니다."));
        if(!customerId.equals(review.getCustomer().getId())) throw new IllegalArgumentException("작성자가 아니므로 삭제가 불가능합니다.");
        reviewRepository.deleteById(reviewId);
    }
}
