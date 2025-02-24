package com.miguel.pruebabackend.infrastructure.adapter;


import com.miguel.pruebabackend.application.service.cartService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CartCleaner {


    private final cartService serviceCart;

    //Constructor
    public CartCleaner(cartService serviceCart) {
        this.serviceCart = serviceCart;
    }

    // Este metodo se ejecuta cada 60 segundos y elimina carritos inactivos
    @Scheduled(fixedRate = 60000)
    public void cleanupInactiveCarts() {
        serviceCart.deleteInactiveCarts();
    }

}
