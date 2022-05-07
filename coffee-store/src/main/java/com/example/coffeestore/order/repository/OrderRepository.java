package com.example.coffeestore.order.repository;

import com.example.coffeestore.order.domain.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    List<Order> findAll();

    Order insert(Order order);

    Optional<Order> findById(UUID orderId);

    void deleteById(UUID orderId);

    void deleteAll();
}
