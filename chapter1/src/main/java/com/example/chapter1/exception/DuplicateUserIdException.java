package com.example.chapter1.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(Throwable cause){
        super(cause);
    }
}
