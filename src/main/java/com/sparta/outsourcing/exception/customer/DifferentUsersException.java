package com.sparta.outsourcing.exception.customer;

public class DifferentUsersException extends RuntimeException {
    public DifferentUsersException(String s) {
        super(s);
    }
}
