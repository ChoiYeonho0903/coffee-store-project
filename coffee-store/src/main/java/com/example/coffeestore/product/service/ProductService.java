package com.example.coffeestore.product.service;


import com.example.coffeestore.product.controller.dto.CreateProductRequestDto;
import com.example.coffeestore.product.controller.dto.ProductResponseDto;
import com.example.coffeestore.product.controller.dto.UpdateProductRequestDto;
import com.example.coffeestore.product.domain.Category;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponseDto> getProductsByCategory(Category category);

    List<ProductResponseDto> getAllProducts();

    CreateProductRequestDto createProduct(CreateProductRequestDto ProductRequestDto);

    UpdateProductRequestDto updateProduct(UpdateProductRequestDto ProductReqeustDto);

    void deleteByProductId (UUID productId);

}
