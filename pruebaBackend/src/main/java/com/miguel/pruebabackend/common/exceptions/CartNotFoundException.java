package com.miguel.pruebabackend.common.exceptions;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message) {
        super(message);
    }
}
