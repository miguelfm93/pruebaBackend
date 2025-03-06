package com.miguel.pruebabackend.application.service;


import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.exceptions.CartNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


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

    @Autowired
    public cartService(cartRepository repositoryCart) {
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
    public Cart getCartById(String id) {
        return repositoryCart.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Carrito no encontrado con id: " + id));
    }
    /**
     * @Method addProducts
     * Agrega uno o más productos al carrito y actualiza el último acceso, si el carrito no existe, se lanza una excepción.
     *
     * @param cartId el identificador del carrito al cual se agregarán los productos.
     * @param products la lista de productos a agregar.
     * @return el carrito actualizado después de guardar los cambios.
     * @throws CartNotFoundException si no se encuentra un carrito con el identificador proporcionado.
     * @throws RuntimeException cuando da error al crear el carrito
     * @throws IllegalArgumentException si se deja la lista vacia
     */
    public Cart addProducts(String cartId, List<Product> products) {
        try {

            // Validación: la lista de productos no puede ser nula o vacía
            if (products == null || products.isEmpty()) {
                throw new IllegalArgumentException("La lista de productos no puede estar vacía.");
            }

            // Se obtiene el carrito, este metodo ya lanza excepción si no se encuentra
            Cart cart = getCartById(cartId);

            // Se añaden los productos al carrito
            cart.addProducts(products);

            log.info("Productos añadidos al carrito {}:", cartId);
            cart.getProducts().forEach(product -> log.info("{}", product));

            // Se guarda el carrito actualizado en el repositorio
            return repositoryCart.save(cart);
        } catch (Exception e) {
            log.error("Error agregando productos al carrito con id {}: {}", cartId, e.getMessage());
            throw new RuntimeException("Error al añadir productos al carrito", e);
        }
    }

    /**
     * @Method deleteCart
     * Elimina un carrito por su ID.
     *
     * @param id Identificador del carrito.
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    public boolean deleteCart(String id) {
        Optional<Cart> cartOpt = repositoryCart.findById(id);
        if (cartOpt.isPresent()) {
            repositoryCart.deleteById(id);
            log.info("Carrito eliminado con ID: {}", id);
            return true;
        }
        return false;
    }

}
