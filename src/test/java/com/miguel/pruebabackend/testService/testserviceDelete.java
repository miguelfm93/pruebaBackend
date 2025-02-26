package com.miguel.pruebabackend.testService;



import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@code cartService}.
 *
 * Se utilizan JUnit 5 y Mockito para simular el comportamiento del repositorio y verificar
 * el correcto funcionamiento de las operaciones de eliminación de carritos.
 *
 * @author Miguel Fernandez
 */

@ExtendWith(MockitoExtension.class)
public class testserviceDelete {


    @InjectMocks
    private cartService cartService;

    @Mock
    private cartRepository cartRepository;



    @Test
    void testDeleteCart_success() {
        // Se simula la eliminación sin excepción
        doNothing().when(cartRepository).delete(1L);

        // Ejecución
        cartService.deleteCart(1L);

        // Verificación
        verify(cartRepository, times(1)).delete(1L);
    }

    @Test
    void testDeleteCart_dataAccessException() {
        // Simula error al eliminar el carrito
        doThrow(new DataAccessException("error") {}).when(cartRepository).delete(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.deleteCart(1L));
        assertEquals("No se pudo eliminar el carrito.", exception.getMessage());
    }

    @Test
    void testDeleteInactiveCarts() {
        // Prepararamos dos carritos: uno inactivo y otro activo
        Cart inactiveCart = new Cart();
        inactiveCart.setId(1L);
        inactiveCart.setLastActivityTime(LocalDateTime.now().minusMinutes(15));

        Cart activeCart = new Cart();
        activeCart.setId(2L);
        activeCart.setLastActivityTime(LocalDateTime.now());

        List<Cart> carts = Arrays.asList(inactiveCart, activeCart);
        when(cartRepository.searchAll()).thenReturn(carts);
        doNothing().when(cartRepository).delete(1L);

        // Ejecución
        cartService.deleteInactiveCarts();

        // Verificación: solo se elimina el carrito inactivo
        verify(cartRepository, times(1)).delete(1L);
        verify(cartRepository, never()).delete(2L);
    }

    @Test
    void testDeleteInactiveCarts_noCarts() {
        // Simula que no hay carritos inactivos
        when(cartRepository.searchAll()).thenReturn(Collections.emptyList());

        // Ejecución
        cartService.deleteInactiveCarts();

        // Verificación: no se llama a delete
        verify(cartRepository, never()).delete(anyLong());
    }
}