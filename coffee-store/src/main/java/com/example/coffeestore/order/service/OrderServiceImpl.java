package com.example.coffeestore.order.service;

import com.example.coffeestore.order.controller.dto.CreateOrderRequestDto;
import com.example.coffeestore.order.controller.dto.OrderResponseDto;
import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public CreateOrderRequestDto createOrder(CreateOrderRequestDto orderRequestDto) {
        Order order = orderRepository.insert(orderRequestDto.toEntity());
        return new CreateOrderRequestDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(order -> new OrderResponseDto(order))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByOrderId(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
