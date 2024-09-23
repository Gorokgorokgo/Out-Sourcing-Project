package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.cart.CartRequestDto;
import com.sparta.outsourcing.dto.cart.CartResponseDto;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.entity.Cart;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.MenuCart;
import com.sparta.outsourcing.repository.CartRepository;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final CustomerRepository customerRepository;
  private final MenuRepository menuRepository;

  public CartResponseDto addCart(AuthUser authUser, CartRequestDto cartRequestDto) {
    // 유저 조회
    Customer customer = customerRepository.findByEmail(authUser.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    // 메뉴 조회
    Menu menu = menuRepository.findById(cartRequestDto.getMenuId())
        .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

    Long quantity = cartRequestDto.getQuantity();
    Long totalPrice = menu.getMenuPrice() * quantity;

    // 장바구니 항목 추가
    Cart cart = new Cart(customer, menu.getStore(), quantity, totalPrice);
    cart.addMenuCart(menu, quantity);

    // 장바구니 저장
    cartRepository.save(cart);

    // 전체 장바구니 목록 조회
    List<Cart> cartList = cartRepository.findAll();

    return new CartResponseDto("장바구니에 추가되었습니다.", cartList);
  }
}
