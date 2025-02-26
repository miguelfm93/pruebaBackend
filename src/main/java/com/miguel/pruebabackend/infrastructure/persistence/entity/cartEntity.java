package com.miguel.pruebabackend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "ULTIMO_ACCESO", length = 20 ,nullable = false)
    @CreationTimestamp
    private LocalDateTime lastActivityTime;

    //La anotación "@OneToMany" establece una relación de uno a muchos entre el carrito y los productos.
    //Elimina automáticamente los productos que ya no estén asociados a ningún carrito
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<productEntity> products = new ArrayList<>();


}
