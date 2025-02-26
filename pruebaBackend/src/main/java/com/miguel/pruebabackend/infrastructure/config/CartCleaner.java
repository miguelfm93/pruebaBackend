package com.miguel.pruebabackend.infrastructure.config;


import com.miguel.pruebabackend.application.service.cartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


@Slf4j
@Configuration
public class CartCleaner {


    private final cartService serviceCart;

    //Constructor
    public CartCleaner(cartService serviceCart) {
        this.serviceCart = serviceCart;
    }

    /**
     * Tarea programada que se ejecuta cada 120 segundos para limpiar carritos inactivos.
     *
     * Registra en el log el inicio de la limpieza de carritos inactivos.
     * Invoca el metodo {@code deleteInactiveCarts} del servicio de carritos para eliminar aquellos que han estado inactivos por mas de 10 min
     */
    @Scheduled(fixedRate = 120000)
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
