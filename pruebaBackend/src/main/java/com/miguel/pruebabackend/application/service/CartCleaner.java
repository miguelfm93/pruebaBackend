package com.miguel.pruebabackend.application.service;


import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;


@Slf4j
@Service
public class CartCleaner {


    private static final long INACTIVITY_LIMIT_MINUTES = 10;

    private final cartRepository repositoryCart;

    public CartCleaner(cartRepository repositoryCart) {
        this.repositoryCart = repositoryCart;
    }


    /**
     * Tarea programada que se ejecuta cada 120 segundos para limpiar carritos inactivos.
     *
     * Registra en el log el inicio de la limpieza de carritos inactivos.
     * Invoca el metodo {@code cleanupInactiveCarts} del servicio de carritos para eliminar aquellos que han estado inactivos por mas de 10 min
     */
    @Scheduled(fixedRate = 120000)
    public void cleanupInactiveCarts() {
        try {
            log.info("Ejecutando limpieza de carritos inactivos...");
            for (Cart cart : repositoryCart.searchAll()) {
                Duration inactivity = Duration.between(cart.getLastActivityTime(), LocalDateTime.now());
                if (inactivity.toMinutes() >= INACTIVITY_LIMIT_MINUTES) {
                    repositoryCart.deleteById(cart.getId());
                    log.info("Carrito inactivo eliminado: " + cart.getId());
                }
                log.info("No hay carritos inactivos para eliminar");
            }
            log.info("Limpieza de carritos inactivos completada.");
        } catch (Exception e) {
            log.error("Error al eliminar carritos inactivos: {}", e.getMessage());
        }
    }

}
