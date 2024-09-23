package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.cart.CartRequestDto;
import com.sparta.outsourcing.dto.cart.CartResponseDto;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Validated
public class CartController {

  private final CartService cartService;

  // 장바구니 추가
  @PostMapping("/add")
  public CartResponseDto addCart(@Auth AuthUser authUser, @Valid @RequestBody CartRequestDto cartRequestDto) {
    return cartService.addCart(authUser, cartRequestDto);
  }

  @DeleteMapping("/delete/{cartId}")
  public CartResponseDto deleteCart(@Auth AuthUser authUser, @PathVariable Long menuId) {
    return cartService.deleteCart(authUser, menuId);
  }
}
