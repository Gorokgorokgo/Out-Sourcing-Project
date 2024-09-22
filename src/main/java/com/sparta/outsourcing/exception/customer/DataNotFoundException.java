package com.sparta.outsourcing.exception.customer;

public class DataNotFoundException extends IllegalArgumentException{
    public DataNotFoundException(String s) {
        super(s);
    }
}
