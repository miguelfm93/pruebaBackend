package com.miguel.pruebabackend.application.service;


import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.domain.port.cartRepositoryPort;
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
        Cart cart = new Cart();
        return repositoryCart.save(cart);
    }

    /**
     * @Method Cart
     * Obtiene la información de un carrito y actualiza el último acceso
     *
     * @param id el identificador del carrito a buscar.
     * @return un -link Optional- que contiene el carrito si existe, - code Optional.empty() - en caso contrario
     */
    public Optional<Cart> getCart(Long id) {
        Optional<Cart> cartOpt = repositoryCart.findById(id);
        cartOpt.ifPresent(cart -> {
            cart.updateLastAccessed();
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
     * @throws RuntimeException si no se encuentra un carrito con el identificador proporcionado.
     */
    public Cart addProducts(Long cartId, List<Product> products) {
        Optional<Cart> optionalCart = repositoryCart.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getProducts().addAll(products);
            cart.updateLastAccessed();
            return repositoryCart.save(cart);
        }
        throw new RuntimeException("Carrito no encontrado");
    }

    /** @Method deleteInactiveCarts
     *
     * Elimina un carrito dado su ID
     * @param cartId el identificador del carrito a eliminar.
     */
    public void deleteCart(Long cartId) {
        repositoryCart.delete(cartId);
    }

    /**
     * @Method deleteInactiveCarts
     *
     * Este metodo recorre la lista de carritos inactivos obtenida del repositorio y elimina aquellos que esten inactivos por 10 minutos.
     */
    public void deleteInactiveCarts() {
        for (Cart cart : repositoryCart.findInactiveCarts()) {
            repositoryCart.delete(cart.getId());
        }
    }

}
