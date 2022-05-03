package com.example.coffeestore.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Order {
    private final UUID orderId;
    private final Email email;
    private String address;
    private String postcode;
    private final List<OrderItem> orderItems;
    private final OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
