package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.Customer;
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


    public CustomerResponseDto(Customer customer) {
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.address = customer.getAddress();
        this.birthday = customer.getBirthday();
        this.roleEnum = customer.getRole();
    }

}
