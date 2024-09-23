//package com.sparta.outsourcing.controller;
//
//import com.sparta.outsourcing.annotation.Auth;
//import com.sparta.outsourcing.dto.cart.CartRequestDto;
//import com.sparta.outsourcing.dto.cart.CartResponseDto;
//import com.sparta.outsourcing.dto.customer.AuthUser;
//import com.sparta.outsourcing.entity.Cart;
//import com.sparta.outsourcing.entity.Customer;
//import com.sparta.outsourcing.entity.Menu;
//import com.sparta.outsourcing.entity.MenuCart;
//import com.sparta.outsourcing.repository.MenuRepository;
//import com.sparta.outsourcing.service.CartService;
//import com.sparta.outsourcing.service.CustomerService;
//import com.sparta.outsourcing.service.MenuService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//@Validated
//public class CartController {
//
//
//  private final CartService cartService;
//  private final MenuService menuService;
//  private final CustomerService customerService;
//
//  // 장바구니 추가
//  @PostMapping("/cart/add")
//  public CartResponseDto addCart(@Auth AuthUser authUser, @Valid @RequestBody CartRequestDto cartRequestDto) {
//
//    // 유저 조회
//    Customer customer = customerService.findUser(authUser.getEmail());
//
//    // 메뉴 조회
//    Menu menu = menuService.findById(cartRequestDto.getMenuId());
//    Long quantity = cartRequestDto.getQuantity();
//    Long totalPrice = menu.getMenuPrice() * quantity;
//
//    // 장바구니 항목 추가
//    Cart cart = new Cart(customer, menu.getStore(), quantity, totalPrice);
//    cart.addMenuCart(menu, cartRequestDto.getQuantity());
//
//    // 장바구니 저장
//    Cart saveCart = cartService.addCart(cart);
//
//    // 전체 장바구니 목록 조회
//    List<Cart> cartList = cartService.getAllCarts();
//
//    return new CartResponseDto("장바구니에 추가되었습니다.", cartList);
//  }
//}
