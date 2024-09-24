package com.sparta.outsourcing.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

  @NotNull(message = "가게 ID는 필수입니다.")
  private Long storeId;

  @NotBlank(message = "배달 주소는 필수입니다.")
  private String deliveryAddress;

  private String request; // 추가 요청사항

  @NotNull(message = "주문 항목은 필수입니다.")
  private List<OrderMenuDto> orderList;
}
