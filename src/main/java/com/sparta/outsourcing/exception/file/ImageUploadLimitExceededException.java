package com.sparta.outsourcing.exception.file;

public class ImageUploadLimitExceededException extends RuntimeException {
    public ImageUploadLimitExceededException(String message) {
        super(message);
    }
}