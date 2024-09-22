package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
