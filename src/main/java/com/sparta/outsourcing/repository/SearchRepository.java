package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findAllByCustomerCustomerIdOrderByCreatedAtDesc(Long customerId);

}
