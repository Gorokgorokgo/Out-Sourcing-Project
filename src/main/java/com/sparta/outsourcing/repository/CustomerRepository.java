package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    Customer findByNaverId(String id);
    Optional<Customer> findByCustomerId(Long customerId);
}
