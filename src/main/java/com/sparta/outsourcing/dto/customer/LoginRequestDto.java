package com.sparta.outsourcing.dto.customer;

import lombok.Getter;

import java.util.Date;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
