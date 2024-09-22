package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByNameLike(String keyword);
}
