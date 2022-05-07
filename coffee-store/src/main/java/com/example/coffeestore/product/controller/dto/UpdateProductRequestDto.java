package com.example.coffeestore.product.controller.dto;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductRequestDto {

    @NotNull
    private UUID productId;

    @NotBlank
    private String productName;

    @NotNull
    private Category category;

    @NotNull
    private long price;

    private String description;

    public UpdateProductRequestDto() {
    }

    public UpdateProductRequestDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }
}
