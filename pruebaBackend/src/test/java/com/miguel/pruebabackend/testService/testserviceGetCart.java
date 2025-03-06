package com.miguel.pruebabackend.testService;


import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@code cartService}.
 *
 * Se utilizan JUnit 5 y Mockito para simular el comportamiento del repositorio y verificar
 * el correcto funcionamiento de las operaciones para obtener un carrito
 *
 * @author Miguel Fernandez
 */

@ExtendWith(MockitoExtension.class)
public class testserviceGetCart {

    @InjectMocks
    private cartService cartService;
    @Mock
    private cartRepository cartRepository;


    @Test
    void testGetCart_notFound() {
        when(cartRepository.findById("1L")).thenReturn(Optional.empty());
        Optional<Cart> result = Optional.ofNullable(cartService.getCartById("1L"));
        assertFalse(result.isPresent());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetCart_found() {
        // Preparamos un carrito existente
        Cart cart = new Cart();
        cart.setId("1L");
        // Inicializamos la lista de productos si es necesario
        cart.setProducts(new ArrayList<>());
        when(cartRepository.findById("1L")).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Ejecución
        Optional<Cart> result = Optional.ofNullable(cartService.getCartById("1L"));

        // Verificación: debe existir y haberse actualizado
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(cartRepository, times(1)).save(cart);
    }
}
