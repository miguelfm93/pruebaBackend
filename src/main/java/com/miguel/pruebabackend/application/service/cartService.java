package com.miguel.pruebabackend.application.service;

import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.domain.port.cartRepositoryPort;
import org.springframework.dao.DataAccessException;
import com.miguel.pruebabackend.common.exceptions.CartNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
public class cartService {

    private final cartRepositoryPort repositoryCart;

    public cartService(cartRepositoryPort repositoryCart) {
        this.repositoryCart = repositoryCart;
    }


    /**
     * @Method createCart
     * Crea un carrito nuevo; el ID se asigna al guardar en la BD
     *
     *  @return el carrito creado y guardado.
     */
    public Cart createCart() {
        try {
            Cart cart = new Cart();
            return repositoryCart.save(cart);
        }catch (DataAccessException e) {
            log.error("Error al crear el carrito: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el carrito.");
        }

    }

    /**
     * @Method Cart
     * Obtiene la información de un carrito y actualiza el último acceso
     *
     * @param id el identificador del carrito a buscar.
     * @return un Optional que contiene el carrito si existe, Optional.empty() en caso contrario.
     */
    public Optional<Cart> getCart(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del carrito no puede ser nulo o negativo.");
        }

        Optional<Cart> cartOpt = repositoryCart.findById(id);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.updateLastAccessed();
            repositoryCart.save(cart);
            log.info("Carrito con ID {} encontrado y actualizado.", id);
        } else {
            log.warn("Carrito con ID {} no encontrado.", id);
        }
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
     */
    public Cart addProducts(Long cartId, List<Product> products) {

        if (cartId == null || cartId <= 0) {
            log.error("ID inválido: {}. Debe ser un número positivo y no nulo.", cartId);
            return null;
        }

        if (products == null || products.isEmpty()) {
            log.error("Lista de productos vacía o nula. No se pueden agregar productos.");
            return null;
        }

        Optional<Cart> optionalCart = repositoryCart.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getProducts().addAll(products);
            cart.updateLastAccessed();
            log.info("Se añadieron {} productos al carrito con ID {}.", products.size(), cartId);
            return repositoryCart.save(cart);
        } else {
            log.warn("Intento de agregar productos a un carrito inexistente (ID {}).", cartId);
            return null;
        }
    }

    /** @Method deleteInactiveCarts
     *
     * Elimina un carrito dado su ID
     * @param cartId el identificador del carrito a eliminar.
     * @throws CartNotFoundException si el carrito no existe.
     */
    public void deleteCart(Long cartId) {

        if (cartId == null || cartId <= 0) {
            log.error("ID inválido: {}. Debe ser un número positivo y no nulo.", cartId);
        }
        try {
            Optional<Cart> cartOpt = repositoryCart.findById(cartId);
            if (cartOpt.isPresent()) {
                repositoryCart.delete(cartId);
                log.info("Carrito con ID {} eliminado correctamente.", cartId);
            } else {
                log.warn("Intento de eliminar un carrito inexistente (ID {}).", cartId);
            }
        }catch(DataAccessException e){
            log.error("Error al eliminar el carrito con ID {}: {}", cartId, e.getMessage());
            throw new RuntimeException("No se pudo eliminar el carrito.");
        }
    }

    /**
     * @Method deleteInactiveCarts
     *
     * Este metodo recorre la lista de carritos inactivos obtenida del repositorio y elimina aquellos que esten inactivos por 10 minutos.
     * @throws RuntimeException si no hay carritos para eliminar.
     */
    public void deleteInactiveCarts() {

        try {

            List<Cart> inactiveCarts = repositoryCart.findInactiveCarts();
            if (inactiveCarts.isEmpty()) {
                log.info("No hay carritos inactivos para eliminar.");
            }

            for (Cart cart : inactiveCarts) {
                repositoryCart.delete(cart.getId());
                log.info("Carrito con ID {} eliminado por inactividad.", cart.getId());
            }
        }  catch (DataAccessException e) {
        log.error("Error al eliminar carritos inactivos: {}", e.getMessage());
        throw new RuntimeException("No se pudo eliminar carritos inactivos.");
        }
    }
}
