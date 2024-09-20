package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.Customers;
import com.sparta.outsourcing.entity.UserRoleEnum;
import lombok.Getter;

import java.util.Date;

@Getter
public class CustomerResponseDto {

    private String name;
    private String email;
    private String address;
    private Date birthday;

    private UserRoleEnum roleEnum;


    public CustomerResponseDto(Customers customers) {
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.address = customers.getAddress();
        this.birthday = customers.getBirthday();
        this.roleEnum = customers.getRole();
    }

}
