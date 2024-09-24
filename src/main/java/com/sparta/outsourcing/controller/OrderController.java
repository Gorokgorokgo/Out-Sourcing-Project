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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Validated
public class OrderController {

  private final OrderService orderService;

  // 주문하기
  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(@Auth AuthUser authUser, @RequestBody OrderRequestDto orderRequestDto) {
    // 인증된 사용자의 정보를 포함하여 주문을 생성
    OrderResponseDto response = orderService.createOrder(authUser, orderRequestDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  // 주문내역 확인하기
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
    OrderResponseDto response = orderService.getOrder(orderId);
    return ResponseEntity.ok(response);
  }
/*
  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    List<OrderResponseDto> response = orderService.getAllOrders();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{orderId}")
  public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
    orderService.deleteOrder(orderId);
    return ResponseEntity.ok("주문이 삭제되었습니다.");
  }*/
}
