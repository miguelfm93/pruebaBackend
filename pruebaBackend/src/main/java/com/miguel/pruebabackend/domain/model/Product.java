package com.miguel.pruebabackend.domain.model;


import lombok.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa una lista de productos.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@NoArgsConstructor
@Data
public class Product {

    // Contador para generar IDs secuenciales
    private static final AtomicInteger counter = new AtomicInteger(0);

    //Atributos
    private String id;
    private String description;
    private Double amount;

    public Product(String description, double amount) {
        this.id = String.valueOf(counter.incrementAndGet());
        this.description = description;
        this.amount = amount;
    }

}
