package com.miguel.pruebabackend.testService;

import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@code cartService}.
 *
 * Se utilizan JUnit 5 y Mockito para simular el comportamiento del repositorio y verificar
 * el correcto funcionamiento de las operaciones de creacion de carritos
 *
 * @author Miguel Fernandez
 */
@ExtendWith(MockitoExtension.class)
public class testserviceCreateCart {

    @Mock
    private cartRepository repositoryCart;

    @InjectMocks
    private cartService service;

    @Test
    void testCreateCart_success() {
        // Preparación: se simula que al guardar se asigna un ID al carrito
        Cart cart = new Cart();
        cart.setId("1L");
        when(repositoryCart.save(any(Cart.class))).thenReturn(cart);

        // Ejecución
        Cart result = service.createCart();

        // Verificación
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repositoryCart, times(1)).save(any(Cart.class));
    }

    @Test
    void testCreateCart_dataAccessException() {
        // Simula un error en el acceso a datos al guardar el carrito
        when(repositoryCart.save(any(Cart.class))).thenThrow(new DataAccessException("error") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createCart());
        assertEquals("No se pudo crear el carrito.", exception.getMessage());
    }
}
