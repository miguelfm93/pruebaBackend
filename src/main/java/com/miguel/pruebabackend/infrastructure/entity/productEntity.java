package com.miguel.pruebabackend.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un producto en la base de datos.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "productos")
public class productEntity {

    //Identicador unico del producto
    @Id
    private Long id;

    private String description;
    private Double amount;

}

