package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.order.OrderResponseDto;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.order.OrderRequestDto;
import com.sparta.outsourcing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Validated
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(@Auth AuthUser authUser, @RequestBody OrderRequestDto orderRequestDto) {
    // 인증된 사용자의 정보를 포함하여 주문을 생성
    OrderResponseDto response = orderService.createOrder(authUser, orderRequestDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

}
