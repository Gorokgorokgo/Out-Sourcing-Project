package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

    Optional<Customers> findByEmail(String email);
}
