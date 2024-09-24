package com.sparta.outsourcing.repository;


import com.sparta.outsourcing.entity.MenuCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuCartRepository extends JpaRepository<MenuCart, Long> {
  Optional<MenuCart> findByCart_CartIdAndMenu_MenuId(Long cartId, Long menuId);

}
