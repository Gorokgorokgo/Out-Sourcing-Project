package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "customer_id")
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "store_id")
  private Store store;

  @Column(nullable = false, name = "delivery_address")
  private String deliveryAddress;

  @Column(nullable = false, name = "total_price")
  private Long totalPrice;

  @Column(nullable = false, name = "request")
  private String request;

  @Column(nullable = false, name = "order_date")
  @CreationTimestamp
  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus = OrderStatus.READY;

  // Order와 Menu의 중간 테이블 관계 추가
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderMenu> orderMenus;  // 중간 테이블 엔티티

  public Order(Customer customer, Store store, String deliveryAddress, String request) {
    this.customer = customer;
    this.store = store;
    this.deliveryAddress = deliveryAddress;
    this.request = request;
  }

  public Order(Customer customer, Store store, String deliveryAddress, String request, Long totalPrice) {
    this.customer = customer;
    this.store = store;
    this.deliveryAddress = deliveryAddress;
    this.request = request;
    this.totalPrice = totalPrice;
  }
}
