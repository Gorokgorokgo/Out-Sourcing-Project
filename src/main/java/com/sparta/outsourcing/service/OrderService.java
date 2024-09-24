package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.order.MenuOrderDto;
import com.sparta.outsourcing.dto.order.OrderRequestDto;
import com.sparta.outsourcing.dto.order.OrderResponseDto;
import com.sparta.outsourcing.entity.*;
import com.sparta.outsourcing.exception.order.OrderNotFoundException;
import com.sparta.outsourcing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final StoreRepository storeRepository;
  private final CustomerRepository customerRepository;
  private final MenuRepository menuRepository;
  private final MenuOrderRepository menuOrderRepository;
  private final CartRepository cartRepository;

  @Transactional
  public OrderResponseDto createOrder(AuthUser authUser, OrderRequestDto orderRequestDto) {
    // 고객 조회
    Customer customer = customerRepository.findByCustomerId(authUser.getCustomerId()).orElseThrow(
        () -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

    // 가게 조회
    Store store = storeRepository.findById(orderRequestDto.getStoreId()).orElseThrow(
        () -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

    // 주문 금액 계산
    Long totalPrice = calculateTotalPrice(orderRequestDto.getOrderList());

    // 최소주문 금액
    if(store.getMinPrice() > totalPrice) {
      return new OrderResponseDto("최소 주문금액을 충족하지 못 했습니다.");
    }

    // 오픈,마감시간
    LocalTime now = LocalTime.now();
    if (now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
      return new OrderResponseDto("가게의 오픈/마감 시간이 지났습니다.");
    }

    // 주문 생성
    Order order = new Order(customer, store, orderRequestDto.getDeliveryAddress(), orderRequestDto.getRequest(), totalPrice);
    orderRepository.save(order);

    // 주문 항목(메뉴) 저장
    for (MenuOrderDto menuOrderDto : orderRequestDto.getOrderList()) {
      Menu menu = findMenuById(menuOrderDto.getMenuId());
      MenuOrder menuOrder = new MenuOrder(order, menu, menuOrderDto.getQuantity());
      menuOrderRepository.save(menuOrder);  // 중간 테이블에 저장
    }

    // 주문 완료 후 장바구니 비우기
    clearCart(customer.getCustomerId());

    // Dto 생성
    return new OrderResponseDto("주문이 완료되었습니다.",
        store.getStoreName(),
        order.getOrderId(),
        order.getOrderStatus().name(),
        totalPrice,
        orderRequestDto.getDeliveryAddress(),
        orderRequestDto.getOrderList());
  }


  // 주문내역 조회
  public OrderResponseDto getOrder(AuthUser authUser, Long orderId) {

    Order order = findByCustomer_CustomerIdAndOrderId(authUser, orderId);

    List<MenuOrderDto> MenuOrders = order.getMenuOrders().stream()
        .map(menuOrder -> new MenuOrderDto(menuOrder.getMenu().getMenuId(), menuOrder.getQuantity()))
        .toList();

    return new OrderResponseDto("주문내역 입니다.",
        order.getStore().getStoreName(),
        orderId,
        order.getOrderStatus().name(),
        order.getTotalPrice(),
        order.getDeliveryAddress(),
        MenuOrders);
  }


  @Transactional
  // 주문내역 수정
  public OrderResponseDto modifyOrder(AuthUser authUser, Long orderId, OrderRequestDto orderRequestDto) {

    Order order = findByCustomer_CustomerIdAndOrderId(authUser, orderId);

    Order updatedOrder = new Order(
        order.getCustomer(),
        order.getStore(),
        orderRequestDto.getDeliveryAddress(),
        orderRequestDto.getRequest(),
        calculateTotalPrice(orderRequestDto.getOrderList())
    );

    List<MenuOrderDto> MenuOrders = order.getMenuOrders().stream()
        .map(menuOrder -> new MenuOrderDto(menuOrder.getMenu().getMenuId(), menuOrder.getQuantity()))
        .toList();

    orderRepository.save(updatedOrder);

    return new OrderResponseDto("주문이 수정되었습니다.",
        order.getStore().getStoreName(),
        updatedOrder.getOrderId(),
        updatedOrder.getOrderStatus().name(),
        updatedOrder.getTotalPrice(),
        updatedOrder.getDeliveryAddress(),
        MenuOrders);
  }

  public void deleteOrder(AuthUser authUser, Long orderId) {

    Order order = findByCustomer_CustomerIdAndOrderId(authUser, orderId);

    orderRepository.delete(order);
  }


///////////////////////////////////////////////////////////////////////////////


  private void clearCart(Long customerId) {
    Cart cart = cartRepository.findByCustomer_CustomerId(customerId).orElseThrow(
        () -> new IllegalArgumentException("장바구니를 찾지 못했습니다."));
    cartRepository.delete(cart);
  }

  private Menu findMenuById(Long menuId) {
    return menuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
  }

  // 주문 금액 계산
  private Long calculateTotalPrice(List<MenuOrderDto> orderList) {
    Long totalPrice = 0L;
    for (MenuOrderDto menuOrderDto : orderList) {
      Menu menu = findMenuById(menuOrderDto.getMenuId());
      totalPrice += menu.getMenuPrice() * menuOrderDto.getQuantity();
    }
    return totalPrice;
  }

  private Order findByCustomer_CustomerIdAndOrderId(AuthUser authUser, Long orderId) {
    return orderRepository.findByCustomer_CustomerIdAndOrderId(authUser.getCustomerId(), orderId).orElseThrow(
        () -> new OrderNotFoundException("주문내역을 찾을 수 없습니다."));
  }
}

