package com.miguel.pruebabackend.infrastructure.persistence;

import com.miguel.pruebabackend.domain.CartRepository.cartRepository;
import com.miguel.pruebabackend.domain.model.Cart;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Repository
@Validated
public class memoryCartRepository implements cartRepository {

    private final ConcurrentHashMap<String, Cart> store = new ConcurrentHashMap<>();
    Cart cart = new Cart();

    @Override
    public Cart save(Cart cart) {
        store.put(cart.getId(), cart);
        return cart;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Void deleteById(String id) {
        store.remove(id);
        return null;
    }

    @Override
    public List<Cart> searchAll() {
        return store.values().stream().collect(Collectors.toList());
    }

}
