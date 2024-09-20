package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
