package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findById(Long orderId);
  Optional<Order> findByCustomer_CustomerIdAndOrderId(Long customerId, Long orderId);
}
