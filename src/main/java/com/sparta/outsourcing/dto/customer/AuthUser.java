package com.sparta.outsourcing.dto.customer;

import com.sparta.outsourcing.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class AuthUser {
    private final String email;
    private final UserRoleEnum role;


    public AuthUser(String userEmail, UserRoleEnum role) {
        this.email = userEmail;
        this.role =  role;
    }
}
