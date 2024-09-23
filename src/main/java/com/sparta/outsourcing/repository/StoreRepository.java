package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 사장님이 운영 중인 가게 수 조회
    int countByCustomer_CustomerIdAndStoreStatus(Long customerId, boolean storeStatus);

    // 개업 중인 상태의 가게와 메뉴 상태가 판매중, 품절인 상태 조회
    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.menus m WHERE s.storeId = :storeId AND s.storeStatus = true AND m.menuStatus IN :statuses")
    Optional<Store> findByIdAndStoreStatusAndMenuStatusIn(@Param("storeId") Long storeId, @Param("statuses") List<MenuStatus> statuses);

    // 가게 다건 조회
    Page<Store> findAllByStoreStatus(boolean storeStatus, Pageable pageable);



}
