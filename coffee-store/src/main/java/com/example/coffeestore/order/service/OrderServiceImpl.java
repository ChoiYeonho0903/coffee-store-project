package com.example.coffeestore.order.service;

import com.example.coffeestore.order.controller.dto.CreateOrderRequestDto;
import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.repository.OrderRepository;
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
}
