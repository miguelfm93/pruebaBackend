package com.miguel.pruebabackend.domain.model;


import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa un carrito de compras.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

//Getters , Setter y constructor generados automaticamente con lombok
@Data
public class Cart {

    // Contador para generar IDs secuenciales
    private static final AtomicInteger counter = new AtomicInteger(0);

    //Atributos
    private String  id;
    private List<Product> products;
    private LocalDateTime lastActivityTime;

    public Cart() {
        this.id = String.valueOf(counter.incrementAndGet());
        this.products = new ArrayList<>();
        this.lastActivityTime = LocalDateTime.now();
    }


    /**
     * Actualiza la última vez que se usó el carrito.
     */
    public void updateLastAccessed(LocalDateTime lastAccessed) {
        this.lastActivityTime = LocalDateTime.now();
    }

    /**
     * Agrega varios productos al carrito.
     *
     * @param product Lista de productos a agregar.
     */
     public void addProducts(List<Product> product) {
        this.products.addAll(product);
        updateLastAccessed(lastActivityTime);
    }



}
