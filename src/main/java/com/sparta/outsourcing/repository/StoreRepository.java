package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영 중인 가게 수 조회
    int countByCustomer_CustomerIdAndStoreStatus(Long customerId, boolean storeStatus);

    Page<Store> findAllByStoreStatus(boolean storeStatus, Pageable pageable);

}
