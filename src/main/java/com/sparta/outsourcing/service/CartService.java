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
import com.sparta.outsourcing.repository.MenuCartRepository;
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
  private final MenuCartRepository menuCartRepository;

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

  public CartResponseDto deleteCart(AuthUser authUser, Long menuId) {
    // 유저 조회
    Customer customer = customerRepository.findByCustomerId(authUser.getCustomerId()).orElseThrow(
        () -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    // 해당 고객의 장바구니 조회
    Cart cart = cartRepository.findByCustomer(customer)
        .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

    // 카트 안에 메뉴 확인
    MenuCart menuCart = menuCartRepository.findByCart_CartIdAndMenu_MenuId(cart.getCartId(), menuId).orElseThrow(
        () -> new IllegalArgumentException("해당 메뉴가 장바구니에 없습니다."));

    // 카트 안에 있는 메뉴 삭제
    menuCartRepository.delete(menuCart);
    return new CartResponseDto("장바구니에서 삭제되었습니다.", menuCart.getMenuCartId());
  }
}
