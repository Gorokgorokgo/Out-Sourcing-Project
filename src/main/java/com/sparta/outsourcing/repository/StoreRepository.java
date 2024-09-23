package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreNameContains(String name);

}
