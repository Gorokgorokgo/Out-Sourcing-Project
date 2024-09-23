package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Page<Menu> findByMenuStatusIn(List<MenuStatus> statuses, Pageable pageable);
}
