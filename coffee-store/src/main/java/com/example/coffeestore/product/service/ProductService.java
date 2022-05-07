package com.example.coffeestore.product.service;


import com.example.coffeestore.product.controller.dto.CreateProductRequestDto;
import com.example.coffeestore.product.controller.dto.ProductResponseDto;
import com.example.coffeestore.product.controller.dto.UpdateProductRequestDto;
import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getProductsByCategory(Category category);

    List<ProductResponseDto> getAllProducts();

    CreateProductRequestDto createProduct(CreateProductRequestDto ProductRequestDto);

    UpdateProductRequestDto updateProduct(UpdateProductRequestDto ProductReqeustDto);

}
