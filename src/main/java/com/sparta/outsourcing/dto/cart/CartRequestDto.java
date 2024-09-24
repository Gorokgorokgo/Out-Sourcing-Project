package com.sparta.outsourcing.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {
  @NotNull(message = "품목을 선택해주세요.")
  private Long menuId;

  @NotNull(message = "장바구니에 음식을 1개 이상 담아주세요.")
  @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
  private Long quantity;
}
