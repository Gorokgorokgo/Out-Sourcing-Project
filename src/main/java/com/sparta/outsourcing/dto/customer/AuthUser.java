package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.constant.UserRoleEnum;
import lombok.Getter;

@Getter
public class AuthUser {
    private final Long customerId;
    private final String email;
    private final UserRoleEnum role;


    public AuthUser(Long customerId, String userEmail, UserRoleEnum role) {
        this.customerId = customerId;
        this.email = userEmail;
        this.role =  role;
    }
}
