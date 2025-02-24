package com.miguel.pruebabackend.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Entidad que representa un carrito en la base de datos.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "carritos")
public class cartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ULTIMO_ACCESO", length = 10 ,nullable = false , unique  = false)
    private LocalDateTime lastAccessed;

    //La anotación "@OneToMany" establece una relación de uno a muchos entre el carrito y los productos.
    //Elimina automáticamente los productos que ya no estén asociados a ningún carrito
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)

    //la anotacion "cart_id" se añadirá en la tabla de productos para establecer la relación con el carrito.
    @JoinColumn(name = "cart_id") // Esta columna se añadirá en la tabla de productos para relacionarla con el carrito
    private List<productEntity> products = new ArrayList<>();

}
