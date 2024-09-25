package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "carts")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private Long cartId;

  @OneToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
  private List<MenuCart> menuCarts = new ArrayList<>();

  @Column(name = "total_quantity")
  private Long totalQuantity;

  @Column(name = "total_price")
  private Long totalPrice;



  public Cart(Customer customer, Store store, Long totalQuantity, Long totalPrice) {
    this.customer = customer;
    this.store = store;
    this.totalQuantity = totalQuantity;
    this.totalPrice = totalPrice;
  }

  public void addMenuCart(Menu menu, Long quantity) {
    if (totalQuantity == null) totalQuantity = 0L;
    if (totalPrice == null) totalPrice = 0L;
    MenuCart menuCart = new MenuCart(menu, this, quantity);
    menuCarts.add(menuCart);
    this.store = menu.getStore();
    this.totalQuantity += quantity;
    this.totalPrice += menu.getMenuPrice() * quantity;
  }
}
