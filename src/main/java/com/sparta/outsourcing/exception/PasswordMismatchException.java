package com.sparta.outsourcing.exception;

public class PasswordMismatchException extends IllegalArgumentException{
    public PasswordMismatchException(String s) {
        super(s);
    }
}
