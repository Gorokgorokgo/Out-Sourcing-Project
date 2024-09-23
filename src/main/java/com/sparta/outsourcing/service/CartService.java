package com.sparta.outsourcing.service;

import com.sparta.outsourcing.entity.Cart;
import com.sparta.outsourcing.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

  public final CartRepository cartRepository;

  @Transactional
  public Cart addCart(Cart cart) {
    try {
      return cartRepository.save(cart);
    } catch (DataAccessException e) {
      throw new RuntimeException("장바구니 추가에 실패했습니다.");
    }
  }

  public List<Cart> getAllCarts() {
    return cartRepository.findAll();
  }
}
