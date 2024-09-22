package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.constant.UserRoleEnum;
import lombok.Getter;

import java.util.Date;

@Getter
public class CustomerResponseDto {

    private String name;
    private String email;
    private String address;
    private Date birthday;

    private UserRoleEnum roleEnum;


    public CustomerResponseDto(Customer customers) {
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.address = customers.getAddress();
        this.birthday = customers.getBirthday();
        this.roleEnum = customers.getRole();
    }

}
