package com.miguel.pruebabackend.infrastructure.adapter;


import com.miguel.pruebabackend.application.service.cartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
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
        try {
            log.info("Ejecutando limpieza de carritos inactivos...");
            serviceCart.deleteInactiveCarts();
            log.info("Limpieza de carritos inactivos completada.");
        } catch (Exception e) {
            log.error("Error al eliminar carritos inactivos: {}", e.getMessage());
        }
    }

}
