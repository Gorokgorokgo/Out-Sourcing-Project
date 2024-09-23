package com.sparta.outsourcing.exception;

public class InvalidAdminTokenException extends RuntimeException {
    public InvalidAdminTokenException(String message) {
        super(message);
    }
}