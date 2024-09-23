package com.sparta.outsourcing.dto.cart;

import com.sparta.outsourcing.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartResponseDto {
  private String message;
  private List<Cart> data;
}
