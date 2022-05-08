package com.example.coffeestore.product.controller.dto;

import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
public class CreateProductRequestDto {

    @NotBlank
    String productName;

    @NotNull
    Category category;

    @NotNull
    long price;

    String description;

    public CreateProductRequestDto() {
    }

    public CreateProductRequestDto(Product product) {
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }

    public Product toEntity() {
        return new Product(UUID.randomUUID(), productName, category, price, description);
    }
}
