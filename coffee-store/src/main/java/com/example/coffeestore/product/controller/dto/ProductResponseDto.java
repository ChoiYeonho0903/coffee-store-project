package com.example.coffeestore.product.controller.dto;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private UUID productId;
    private String productName;
    private Category category;
    private long price;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }
}
