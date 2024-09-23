package com.sparta.outsourcing.exception.store;

public class MaxStoreLimitReachedException extends RuntimeException {
    public MaxStoreLimitReachedException(String message) {
        super(message);
    }
}
