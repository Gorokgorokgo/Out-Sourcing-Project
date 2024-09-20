package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.customer.Customers;
import lombok.Getter;

import java.util.Date;

@Getter
public class CustomerResponseDto {

    private String name;
    private String email;
    private String address;
    private Date birthday;


    public CustomerResponseDto(Customers customers) {
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.address = customers.getAddress();
        this.birthday = customers.getBirthday();
    }

}
