package com.miguel.pruebabackend.application.service;

import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.infrastructure.persistence.entity.cartEntity;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import com.miguel.pruebabackend.common.exceptions.CartNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Servicio que maneja la lógica de negocio relacionada con la gestión de carritos de compra.
 * Este servicio consta de los siguientes métodos:
 *
 *   Crear un nuevo carrito.
 *   Obtener un carrito por su identificador y actualizar su marca de último acceso.
 *   Agregar productos a un carrito existente y actualizar su marca de último acceso.
 *   Eliminar un carrito dado su identificador.
 *   Eliminar automáticamente los carritos inactivos (pasados 10 minutos)
 *
 * La implementación se basa en un repositorio definido por la interfaz - CartRepositoryPort -.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@Slf4j
@Service
public class cartService  {

    private final cartRepository repositoryCart;
    private final LocalDateTime now = LocalDateTime.now();


    public cartService(cartRepository repositoryCart) {
        this.repositoryCart = repositoryCart;
    }


    /**
     * @Method createCart
     * Crea un carrito nuevo; el ID se asigna al guardar en la BD
     *
     *  @return el carrito creado y guardado.
     */
    @Transactional
    public Cart createCart() {
        try {
            Cart cart = new Cart();
            Cart savedCart = repositoryCart.save(cart);
            log.info("Carrito creado y guardado con éxito, ID: {}", savedCart.getId());
            return savedCart;
        } catch (DataAccessException e) {
            log.error("Error al crear el carrito: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el carrito.");
        }

    }

    /**
     * @param id el identificador del carrito a buscar.
     * @return un Optional que contiene el carrito si existe, Optional.empty() en caso contrario.
     * @Method Cart
     * Obtiene la información de un carrito y actualiza el último acceso
     */
    @Transactional
    // Obtiene la información de un carrito y actualiza el último acceso
    public Optional<Cart> getCart(Long id) {
        Optional<Cart> cartOpt = repositoryCart.findById(id);
        cartOpt.ifPresent(cart -> {
            cart.updateLastAccessed(now);
            repositoryCart.save(cart);
        });
        return cartOpt;
    }
    /**
     * @Method addProducts
     * Agrega uno o más productos al carrito y actualiza el último acceso, si el carrito no existe, se lanza una excepción.
     *
     * @param cartId el identificador del carrito al cual se agregarán los productos.
     * @param products la lista de productos a agregar.
     * @return el carrito actualizado después de guardar los cambios.
     * @throws CartNotFoundException si no se encuentra un carrito con el identificador proporcionado.
     * @throws IllegalArgumentException cuando no se recibe un parametro correcto y/o valido
     */
    @Transactional
    // Agrega uno o más productos al carrito y actualiza el último acceso
    public Cart addProducts(Long cartId, List<Product> products) {
        Optional<Cart> optionalCart = repositoryCart.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getProducts().addAll(products);
            cart.updateLastAccessed(now);
            return repositoryCart.save(cart);
        }
        throw new RuntimeException("Carrito no encontrado");
    }

    /** @Method deleteInactiveCarts
     *
     * Elimina un carrito dado su ID
     * @param cartId el identificador del carrito a eliminar.
     * @throws CartNotFoundException si el carrito no existe.
     */
    @Transactional
    public void deleteCart(Long cartId) {
        try {
                repositoryCart.delete(cartId);
                log.info("Carrito con id {} borrado correctamente", cartId);

        } catch (DataAccessException e) {
            log.error("Error al eliminar el carrito con ID {}: {}", cartId, e.getMessage());
            throw new RuntimeException("No se pudo eliminar el carrito.", e);
        }
    }

    /**
     * @Method deleteInactiveCarts
     *
     * Este metodo recorre la lista de carritos inactivos obtenida del repositorio y elimina aquellos que esten inactivos por 10 minutos.
     * @throws RuntimeException si no hay carritos para eliminar.
     */
    @Transactional
    public void deleteInactiveCarts() {

        try {

            LocalDateTime limite = LocalDateTime.now().minusMinutes(10);

            List<Cart> inactiveCarts = repositoryCart.searchAll();
            if (inactiveCarts.isEmpty()) {
                log.info("No hay carritos inactivos para eliminar.");
            }

            for (Cart cart : inactiveCarts) {
                if (cart.getLastActivityTime().isBefore(limite)) {
                repositoryCart.delete(cart.getId());
                log.info("Carrito con ID {} eliminado por inactividad.", cart.getId());
                }
            }
        }  catch (DataAccessException e) {
        log.error("Error al eliminar carritos inactivos: {}", e.getMessage());
        throw new RuntimeException("No se pudo eliminar carritos inactivos.");
        }
    }


}
