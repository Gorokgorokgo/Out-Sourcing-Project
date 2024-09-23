package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
//    @Query("select s from store s where name regex 'keword'")
//    List<Store> findAllByNameQuery(String keyword);
//    List<Store> findAllByNameLike(String name);
    List<Store> findAllByNameContains(String name);

}
