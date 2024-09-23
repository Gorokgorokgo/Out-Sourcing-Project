package com.sparta.outsourcing.dto.cart;

import com.sparta.outsourcing.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
  private String message;
  private List<Cart> data = new ArrayList<>();
  private Long menuCartId;


  public CartResponseDto(String message, List<Cart> data) {
    this.message = message;
    this.data = (data != null) ? data : new ArrayList<>(); // null 체크
  }
  public CartResponseDto(String message, Long menuCartId) {
    this.menuCartId = menuCartId;
    this.message = message;
    this.data = new ArrayList<>();
  }
}
