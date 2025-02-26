package com.miguel.pruebabackend.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Clase que representa un carrito de compras.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

//Getters , Setter y constructor generados automaticamente con lombok
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart {

    //Atributos
    private Long id;
    private List<Product> products = new ArrayList<>();
    private LocalDateTime lastActivityTime;


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
