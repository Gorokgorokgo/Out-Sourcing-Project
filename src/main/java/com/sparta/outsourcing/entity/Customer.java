package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "CUSTOMERS")
@Getter
public class Customer extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
