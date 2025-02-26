package com.miguel.pruebabackend.infrastructure.persistence.adapter;

import com.miguel.pruebabackend.common.exceptions.CartNotFoundException;
import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.infrastructure.persistence.entity.cartEntity;
import com.miguel.pruebabackend.infrastructure.persistence.entity.productEntity;
import com.miguel.pruebabackend.infrastructure.persistence.repository.SpringDataCartRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Repository
public class CartRepositoryAdapter implements cartRepository {

    private final SpringDataCartRepository repository;


    public CartRepositoryAdapter(SpringDataCartRepository repository) {
        this.repository = repository;
    }


    @Override
    public Cart save(Cart cart) {

        // Obtenemos la entidad existente o creamos una nueva
        cartEntity entity = (cart.getId() != null)
                ? repository.findById(cart.getId()).orElse(new cartEntity())
                : new cartEntity();

        // Actualizamos el campo de la última actividad
        entity.setLastActivityTime(cart.getLastActivityTime());

        // Convertimos la lista de productos del dominio a entidades JPA
        List<productEntity> productEntities = cart.getProducts().stream()
                .map(product -> {
                    productEntity pe = new productEntity();
                    pe.setId(product.getId());
                    pe.setDescription(product.getDescription());
                    pe.setAmount(product.getAmount());
                    return pe;
                })
                .collect(Collectors.toList());
        entity.getProducts().clear();
        entity.getProducts().addAll(productEntities);

        // Guardamos la entidad (se actualiza o inserta según corresponda)
        cartEntity saved = repository.save(entity);

        // Mapeamos la entidad guardada de vuelta al modelo de dominio
        Cart savedCart = new Cart();
        savedCart.setId(saved.getId());
        savedCart.setLastActivityTime(saved.getLastActivityTime());
        List<Product> domainProducts = saved.getProducts().stream()
                .map(pe -> new Product(pe.getId(), pe.getDescription(), pe.getAmount()))
                .collect(Collectors.toList());
        savedCart.setProducts(domainProducts);

        return savedCart;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return repository.findById(id).map(entity -> {
            Cart cart = new Cart();
            cart.setId(entity.getId());
            cart.setLastActivityTime(entity.getLastActivityTime());
            List<Product> products = entity.getProducts().stream().map(pe ->
                    new Product(pe.getId(), pe.getDescription(), pe.getAmount())
            ).collect(Collectors.toList());
            cart.setProducts(products);
            return cart;
        });

    }

    @Override
    public void delete(Long cartId) {
        if (cartId == null) {
            throw new IllegalArgumentException("El id del carrito no puede ser null");
        }

        cartEntity cartToDelete = repository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("No se encontró el carrito con ID " + cartId));

        repository.delete(cartToDelete);
    }

    @Override
    public List<Cart> searchAll() {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(10);
        List<cartEntity> entities = repository.findBylastActivityTimeBefore(limit);
        return entities.stream().map(entity -> {
            Cart cart = new Cart();
            cart.setId(entity.getId());
            cart.setLastActivityTime(entity.getLastActivityTime());
            List<Product> products = entity.getProducts().stream().map(pe ->
                    new Product(pe.getId(), pe.getDescription(), pe.getAmount())
            ).collect(Collectors.toList());
            cart.setProducts(products);
            return cart;
        }).collect(Collectors.toList());
    }

}
