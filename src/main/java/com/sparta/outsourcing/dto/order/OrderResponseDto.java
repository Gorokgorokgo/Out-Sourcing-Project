package com.sparta.outsourcing.dto.order;

import com.sparta.outsourcing.entity.Order;
import com.sparta.outsourcing.entity.OrderMenu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter

public class OrderResponseDto {

  private String message;
  private String storeName;
  private Long orderId;
  private String orderStatus;
  private Long totalPrice;
  private String deliveryAddress;
  private List<OrderMenuDto> orderList;

  public OrderResponseDto(String message, String storeName, Long orderId, String orderStatus,
                          Long totalPrice, String deliveryAddress, List<OrderMenuDto> orderList) {
    this.message = message;
    this.storeName = storeName;
    this.orderId = orderId;
    this.orderStatus = orderStatus;
    this.totalPrice = totalPrice;
    this.deliveryAddress = deliveryAddress;
    this.orderList = orderList != null ? orderList : new ArrayList<>();
  }

  public OrderResponseDto(String message, String storeName) {
    this.message = message;
    this.storeName = storeName;
    this.orderList = new ArrayList<>();
  }

  public OrderResponseDto(String message) {
    this.message = message;
  }
}
