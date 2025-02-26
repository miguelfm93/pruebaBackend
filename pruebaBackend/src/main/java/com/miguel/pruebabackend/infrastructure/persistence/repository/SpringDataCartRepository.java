package com.miguel.pruebabackend.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;
import com.miguel.pruebabackend.infrastructure.persistence.entity.cartEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface  SpringDataCartRepository extends JpaRepository < cartEntity , Long > {

    /**
     * Busca y retorna una lista de carritos cuyo último acceso es anterior a la fecha y hora indicadas.
     * <p>
     * Este metodo se utiliza para identificar los carritos que han estado inactivos durante un período
     * determinado y que pueden ser eliminados para liberar recursos.
     * </p>
     *
     * @param dateTime la fecha y hora límite. Se retornarán los carritos cuyo último acceso sea anterior a este valor.
     * @return una lista de instancias de {@code cartEntity} que cumplen la condición de inactividad.
     */
    List<cartEntity> findBylastActivityTimeBefore(LocalDateTime dateTime);

}
