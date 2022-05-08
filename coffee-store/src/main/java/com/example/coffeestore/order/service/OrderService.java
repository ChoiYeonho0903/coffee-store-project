package com.example.coffeestore.order.service;

import com.example.coffeestore.order.controller.dto.CreateOrderRequestDto;
import com.example.coffeestore.order.controller.dto.OrderResponseDto;
import com.example.coffeestore.order.domain.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    CreateOrderRequestDto createOrder(CreateOrderRequestDto orderRequestDto);
    List<OrderResponseDto> getAllOrders();
    void deleteByOrderId(UUID orderId);
}
