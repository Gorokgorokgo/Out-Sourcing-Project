package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "menus_orders")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuOrderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "menu_id")
  private Menu menu;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "order_id")
  private Order order;
}
