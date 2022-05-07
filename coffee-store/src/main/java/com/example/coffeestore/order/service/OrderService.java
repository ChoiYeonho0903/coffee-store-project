package com.example.coffeestore.order.service;

import com.example.coffeestore.order.controller.dto.CreateOrderRequestDto;
import com.example.coffeestore.order.domain.Order;

public interface OrderService {
    CreateOrderRequestDto createOrder(CreateOrderRequestDto orderRequestDto);
}
