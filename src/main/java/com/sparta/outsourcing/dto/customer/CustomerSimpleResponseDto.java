package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerSimpleResponseDto {
    private Long customerId;
    private String customerName;

    public CustomerSimpleResponseDto(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.customerName = customer.getName();
    }
}
