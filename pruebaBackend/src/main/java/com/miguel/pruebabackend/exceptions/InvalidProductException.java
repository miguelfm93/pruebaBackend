package com.miguel.pruebabackend.exceptions;

public class InvalidProductException extends RuntimeException{

    public InvalidProductException(String message) {
        super(message);
    }
}
