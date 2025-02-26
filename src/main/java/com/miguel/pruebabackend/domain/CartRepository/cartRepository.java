package com.miguel.pruebabackend.domain.CartRepository;

import com.miguel.pruebabackend.domain.model.Cart;


import java.util.Optional;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia para objetos de tipo Cart.
 *
 * Esta interfaz forma parte de la capa de dominio y contiene operaciones
 * básicas sobre los carritos, permitiendo guardar, buscar, eliminar y obtener aquellos carritos
 * que se consideran inactivos.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

public interface cartRepository  {

    /**
     * Guarda o actualiza un objeto de tipo {@link Cart} en la base de datos.
     * Si el carrito no existe (por ejemplo, su identificador es null), se crea un nuevo registro.
     * Si ya existe, se actualiza la información correspondiente.
     *
     * @param cart el objeto {@link Cart} a guardar o actualizar.
     * @return el objeto {@link Cart} guardado, con su identificador asignado o actualizado.
     */
    Cart save(Cart cart);

    /**
     * Busca un carrito por su identificador.
     *
     * @param id el identificador del carrito a buscar.
     * @return un {@link Optional} que contiene el carrito encontrado, o {@code Optional.empty()} si no se encuentra.
     */
    Optional<Cart> findById(Long id);

    /**
     * Elimina un carrito de la base de datos utilizando su identificador.
     * @param id el identificador del carrito a eliminar.
     */
    void delete(Long id);

    /**
     * Retorna una lista de carritos inactivos para su borrado.
     * Inactivos aquellos carritos que han estado sin actividad durante más de 10 minutos.
     * @return una lista de objetos {@link Cart} que cumplen la condición de inactividad.
     */
    List<Cart> searchAll();
}
