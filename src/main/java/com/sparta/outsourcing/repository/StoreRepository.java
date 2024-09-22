package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
//    @Query("select * from store where name regexp 'keword'and address ")
//    List<Store> findAllByNameQuery(String keyword);
}
