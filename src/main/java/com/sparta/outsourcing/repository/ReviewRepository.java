package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Review;
import com.sparta.outsourcing.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByStoreAndStarBetween(Store store, int minStar, int maxStar, Pageable pageable);
}
