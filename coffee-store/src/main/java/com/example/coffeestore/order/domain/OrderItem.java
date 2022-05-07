package com.example.coffeestore.order.domain;

import com.example.coffeestore.product.domain.Category;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderItem {
    long id;
    UUID orderId;
    UUID productId;
    Category category;
    long price;
    int quantity;

    public OrderItem(long id, UUID orderId, UUID productId,
        Category category, long price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
