package com.sparta.outsourcing.exception;

public class ImageUploadLimitExceededException extends RuntimeException {
    public ImageUploadLimitExceededException(String message) {
        super(message);
    }
}