package com.miguel.pruebabackend.infrastructure.controller;

import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


/**
 * Controlador REST para la gestión de carritos de compras.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar carritos.
 *
 * @Author Miguel Fernandez
 * @version 1.0
 */

@Slf4j
@RestController
@Validated
@RequestMapping("/api/carts")
public class cartController {

    private final cartService serviceCart;

    /**
     * Constructor de la clase CartController.
     * @param serviceCart Servicio de carrito que se inyecta para manejar la lógica de negocio.
     */
    @Autowired
    public cartController(cartService serviceCart) {
        this.serviceCart = serviceCart;

    }

    /**
     * Endpint: Crea un nuevo carrito de compras.
     * @return ResponseEntity con el carrito creado y código de estado 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = serviceCart.createCart();
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }


    /**
     * Endpoint: Obtiene la información de un carrito dado su identificador.
     *
     * @param id Identificador único del carrito (debe ser mayor o igual a 1).
     * @return ResponseEntity con el carrito encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable String id) {
        try {
            Cart cart = serviceCart.getCartById(id);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint que agrega uno o más productos a un carrito existente.
     *
     * @param id Identificador del carrito al que se agregarán los productos (debe ser mayor o igual a 1).
     * @param products Lista de productos a agregar. No puede estar vacía.
     * @return ResponseEntity con el carrito actualizado.
     */
    @PostMapping("/{id}/products")
    public ResponseEntity<Cart> addProducts(@PathVariable String id, @RequestBody List<Product> products) {
        try {
            Cart updatedCart = serviceCart.addProducts(id, products);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para eliminar un carrito manualmente por su ID.
     * @param id Identificador del carrito.
     * @return Estado OK si se eliminó o NOT FOUND en caso contrario.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        if (serviceCart.deleteCart(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
