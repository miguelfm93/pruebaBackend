package com.miguel.pruebabackend.infrastructure.api;

import com.miguel.pruebabackend.common.exceptions.CartNotFoundException;
import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.common.exceptions.InvalidProductException;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;

import com.miguel.pruebabackend.infrastructure.persistence.adapter.CartRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de carritos de compras.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar carritos.
 *
 * @Author Miguel Fernandez
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/api/carts")
public class cartController {

    private final cartService serviceCart;

    /**
     * Constructor de la clase CartController.
     * @param serviceCart Servicio de carrito que se inyecta para manejar la lógica de negocio.
     */
    public cartController(cartService serviceCart) {
        this.serviceCart = serviceCart;

    }

    /**
     * Crea un nuevo carrito de compras.
     * @return ResponseEntity con el carrito creado y código de estado 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = serviceCart.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    /**
     * Obtiene la información de un carrito dado su identificador.
     *
     * @param id Identificador único del carrito (debe ser mayor o igual a 1).
     * @return ResponseEntity con el carrito encontrado o una excepción si no existe.
     * @throws CartNotFoundException si el carrito con el ID proporcionado no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional> getCart(@PathVariable Long id) {
            Optional cart = serviceCart.getCart(id);
            // Si se encuentra el carrito, se devuelve con un 200 OK
            return ResponseEntity.ok(cart);
    }

    /**
     * Agrega uno o más productos a un carrito existente.
     *
     * @param id Identificador del carrito al que se agregarán los productos (debe ser mayor o igual a 1).
     * @param products Lista de productos a agregar. No puede estar vacía.
     * @return ResponseEntity con el carrito actualizado.
     * @throws InvalidProductException si la lista de productos está vacía.
     */
    @PostMapping("/{id}/products")
    public ResponseEntity<Cart> addProducts(@PathVariable @Min(1) Long id, @RequestBody @Valid List<Product> products) {
        Cart cart = serviceCart.addProducts(id, products);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cart);
    }

    /**
     * Elimina un carrito de compras por su identificador.
     *
     * @param id Identificador único del carrito (debe ser mayor o igual a 1).
     * @return ResponseEntity con estado 204 (NO CONTENT) si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable @Min(1) long id) {
        serviceCart.deleteCart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
