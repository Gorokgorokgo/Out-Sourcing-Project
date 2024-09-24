package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.order.OrderRequestDto;
import com.sparta.outsourcing.dto.order.OrderResponseDto;
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

  // 주문하기
  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(@Auth AuthUser authUser, @RequestBody OrderRequestDto orderRequestDto) {
    // 인증된 사용자의 정보를 포함하여 주문을 생성
    OrderResponseDto response = orderService.createOrder(authUser, orderRequestDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  // 주문내역 조회
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
    OrderResponseDto response = orderService.getOrder(orderId);
    return ResponseEntity.ok(response);
  }

  // 주문내역 수정
  @PutMapping("/modify/{orderId}")  // @PathVariable 추가
  public ResponseEntity<OrderResponseDto> modifyOrder(@PathVariable Long orderId, @RequestBody OrderRequestDto orderRequestDto) {
    OrderResponseDto response = orderService.modifyOrder(orderId, orderRequestDto);
    return ResponseEntity.ok(response);
  }

  // 주문내역 삭제
  @DeleteMapping("/{orderId}")
  public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
    orderService.deleteOrder(orderId);
    return ResponseEntity.ok("주문이 삭제되었습니다.");
  }
}
