package com.miguel.pruebabackend.infrastructure.adapter;

import com.miguel.pruebabackend.domain.model.Cart;
import com.miguel.pruebabackend.domain.model.Product;
import com.miguel.pruebabackend.domain.port.cartRepositoryPort;
import com.miguel.pruebabackend.infrastructure.entity.cartEntity;
import com.miguel.pruebabackend.infrastructure.entity.productEntity;
import com.miguel.pruebabackend.infrastructure.repository.SpringDataCartRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CartRepositoryAdapter implements cartRepositoryPort {

    private final SpringDataCartRepository repository;


    public CartRepositoryAdapter(SpringDataCartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart save(Cart cart) {
        cartEntity entity;
        if (cart.getId() != null) {
            entity = repository.findById(cart.getId()).orElse(new cartEntity());
        } else {

            entity = new cartEntity();
        }
        entity.setLastAccessed(cart.getLastAccessed());

        // Convierte la lista de productos del dominio a entidades JPA
        List<productEntity> productEntities = cart.getProducts().stream().map(product -> {
            productEntity pe = new productEntity();
            pe.setId(product.getId());
            pe.setDescription(product.getDescription());
            pe.setAmount(product.getAmount());
            return pe;
        }).collect(Collectors.toList());
        entity.getProducts().clear();
        entity.getProducts().addAll(productEntities);

        cartEntity saved = repository.save(entity);

        // Mapea la entidad guardada de vuelta al modelo de dominio
        Cart savedCart = new Cart();
        savedCart.setId(saved.getId());
        savedCart.setLastAccessed(saved.getLastAccessed());
        List<Product> domainProducts = saved.getProducts().stream().map(pe ->
                new Product(pe.getId(), pe.getDescription(), pe.getAmount())
        ).collect(Collectors.toList());
        savedCart.setProducts(domainProducts);
        return savedCart;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Cart> findInactiveCarts() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<cartEntity> entities = repository.findByLastAccessedBefore(threshold);
        return entities.stream().map(entity -> {
            Cart cart = new Cart();
            cart.setId(entity.getId());
            cart.setLastAccessed(entity.getLastAccessed());
            List<Product> products = entity.getProducts().stream().map(pe ->
                    new Product(pe.getId(), pe.getDescription(), pe.getAmount())
            ).collect(Collectors.toList());
            cart.setProducts(products);
            return cart;
        }).collect(Collectors.toList());
    }
}
