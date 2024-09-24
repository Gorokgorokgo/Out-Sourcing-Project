package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.dto.cart.CartResponseDto;
import com.sparta.outsourcing.entity.Cart;
import com.sparta.outsourcing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByCustomer(Customer customer);

  Optional<Cart> findByCustomer_CustomerId(Long customerId);
}
