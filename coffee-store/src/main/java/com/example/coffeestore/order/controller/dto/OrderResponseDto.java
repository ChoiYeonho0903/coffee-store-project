package com.example.coffeestore.order.controller.dto;

import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.domain.OrderItem;
import com.example.coffeestore.order.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private UUID orderId;
    private String email;
    private String address;
    private String postcode;
    private List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.email = order.getEmail();
        this.address = order.getAddress();
        this.postcode = order.getPostcode();
        this.orderItems = order.getOrderItems();
        this.orderStatus = order.getOrderStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }
}
