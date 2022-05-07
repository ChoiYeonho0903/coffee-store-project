package com.example.coffeestore.order.controller.dto;

import com.example.coffeestore.order.domain.Order;
import com.example.coffeestore.order.domain.OrderItem;
import com.example.coffeestore.order.domain.OrderStatus;
import com.example.coffeestore.product.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateOrderRequestDto {

    @NotNull
    @Email
    String email;

    @NotBlank
    String address;

    @NotBlank
    String postcode;

    @NotNull
    List<OrderItem> orderItems;

    public CreateOrderRequestDto() {
    }

    public CreateOrderRequestDto(Order order) {
        this.email = order.getEmail();
        this.address = order.getAddress();
        this.postcode = order.getPostcode();
        this.orderItems = order.getOrderItems();
    }

    public Order toEntity() {
        return new Order(
            UUID.randomUUID(),
            this.email,
            this.address,
            this.postcode,
            this.orderItems,
            OrderStatus.ACCEPTED);
    }
}
