package com.sparta.outsourcing.exception;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(String message) {

    super(message);

  }
}
