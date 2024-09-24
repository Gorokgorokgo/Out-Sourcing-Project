package com.sparta.outsourcing.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuOrderDto {

  @NotNull(message = "메뉴 ID는 필수입니다.")
  private Long menuId; // 메뉴 ID

  @NotNull(message = "수량은 필수입니다.")
  private Long quantity; // 수량
}
