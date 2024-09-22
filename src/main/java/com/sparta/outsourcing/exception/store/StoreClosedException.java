package com.sparta.outsourcing.exception.store;

public class StoreClosedException extends RuntimeException {
    public StoreClosedException(String message) {
        super(message);
    }
}
