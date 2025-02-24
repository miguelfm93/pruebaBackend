package com.miguel.pruebabackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

/**
 * Clase que representa un carrito de compras.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

//Getters , Setter y constructor generados automaitacmente con lombok
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart {

    //Atributos
    private Long id;
    private List<Product> products;
    private LocalDateTime lastAccessed;


    // Actualiza la fecha y hora del último acceso
    public void updateLastAccessed() {
        this.lastAccessed = LocalDateTime.now();
    }
}
