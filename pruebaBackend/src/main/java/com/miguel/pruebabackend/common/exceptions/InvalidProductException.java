package com.miguel.pruebabackend.common.exceptions;

public class InvalidProductException extends RuntimeException{

    public InvalidProductException(String message) {
        super(message);
    }
}
