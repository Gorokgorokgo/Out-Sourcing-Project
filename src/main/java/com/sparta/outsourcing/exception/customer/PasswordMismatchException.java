package com.sparta.outsourcing.exception.customer;

public class PasswordMismatchException extends IllegalArgumentException{
    public PasswordMismatchException(String s) {
        super(s);
    }
}
