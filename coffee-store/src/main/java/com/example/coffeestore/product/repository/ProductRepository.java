package com.example.coffeestore.product.repository;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    List<Product> findAll();

    Product insert(Product product);

    Product update(Product product);

    Optional<Product> findById(UUID productId);

    Optional<Product> findByName(String productName);

    List<Product> findByCategory(Category category);

    void deleteById(UUID productId);

    void deleteAll();

}
