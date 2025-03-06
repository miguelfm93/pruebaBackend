package com.miguel.pruebabackend.testService;

import com.miguel.pruebabackend.application.service.cartService;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@code cartService}.
 *
 * Se utilizan JUnit 5 y Mockito para simular el comportamiento del repositorio y verificar
 * el correcto funcionamiento de las operaciones para añadir productos a carritos
 *
 * @author Miguel Fernandez
 */
@ExtendWith(MockitoExtension.class)
public class testserviceAddProducts {

    @InjectMocks
    private cartService cartService;

    @Mock
    private cartRepository cartRepository;

    @Test
    void testAddProducts_cartNotFound() {
        when(cartRepository.findById("1L")).thenReturn(Optional.empty());
        Product p = new Product();
        List<Product> products = Collections.singletonList(p);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.addProducts("1L", products));
        assertEquals("Carrito no encontrado", exception.getMessage());
    }

    @Test
    void testAddProducts_success() {
        // Preparamos un carrito con lista de productos inicializada
        Cart cart = new Cart();
        cart.setId("1L");
        cart.setProducts(new ArrayList<>());
        when(cartRepository.findById("1L")).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Simulamos la lista de productos a agregar
        Product p1 = new Product();
        Product p2 = new Product();
        List<Product> products = Arrays.asList(p1, p2);

        // Ejecución
        Cart result = cartService.addProducts("1L", products);

        // Verificación: se deben haber agregado 2 productos
        assertNotNull(result);
        assertEquals(2, result.getProducts().size());
        verify(cartRepository, times(1)).save(cart);
    }
}
