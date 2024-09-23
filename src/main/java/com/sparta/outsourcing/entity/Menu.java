package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "menus")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "store_id")
  private Store store;

  @Column(nullable = false)
  private String menuName;

  @Column(nullable = false)
  private int menuPrice;
}
