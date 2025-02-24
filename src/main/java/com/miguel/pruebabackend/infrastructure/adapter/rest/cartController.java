package com.miguel.pruebabackend.infrastructure.adapter.rest;


import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class cartController {

    private final cartService serviceCart;

    //Constructor
    public cartController(cartService serviceCart) {
        this.serviceCart = serviceCart;
    }

    // Endpoint para crear un carrito nuevo
    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = serviceCart.createCart();
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    // Endpoint para obtener la información de un carrito según su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        return serviceCart.getCart(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para agregar uno o más productos a un carrito
    @PostMapping("/{id}/products")
    public ResponseEntity<Cart> addProducts(@PathVariable Long id, @RequestBody List<Product> products) {
        try {
            Cart updatedCart = serviceCart.addProducts(id, products);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar un carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        serviceCart.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
