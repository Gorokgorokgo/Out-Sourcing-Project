package com.sparta.outsourcing.exception;

public class DataNotFoundException extends IllegalArgumentException{
    public DataNotFoundException(String s) {
        super(s);
    }
}
