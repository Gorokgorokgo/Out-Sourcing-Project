package com.sparta.outsourcing.exception.customer;

public class InvalidAdminTokenException extends RuntimeException {
    public InvalidAdminTokenException(String message) {
        super(message);
    }
}