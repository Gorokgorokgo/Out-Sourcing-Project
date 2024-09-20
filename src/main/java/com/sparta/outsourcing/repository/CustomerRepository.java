package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
