package com.miguel.pruebabackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una lista de productos.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    //Atributos
    private Long id;
    private String description;
    private Double amount;

}
