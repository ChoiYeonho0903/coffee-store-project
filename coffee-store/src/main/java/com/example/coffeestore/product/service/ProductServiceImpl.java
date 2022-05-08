package com.example.coffeestore.product.service;

import com.example.coffeestore.product.controller.dto.CreateProductRequestDto;
import com.example.coffeestore.product.controller.dto.ProductResponseDto;
import com.example.coffeestore.product.controller.dto.UpdateProductRequestDto;
import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.domain.Product;
import com.example.coffeestore.product.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDto> getProductsByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
            .map(product -> new ProductResponseDto(product))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(product -> new ProductResponseDto(product))
            .collect(Collectors.toList());
    }

    @Override
    public CreateProductRequestDto createProduct(CreateProductRequestDto ProductRequestDto) {
        return new CreateProductRequestDto(productRepository.insert(ProductRequestDto.toEntity()));
    }

    @Override
    public UpdateProductRequestDto updateProduct(UpdateProductRequestDto updateProductRequestDto) {
        Product product = productRepository.findById(updateProductRequestDto.getProductId()).get();

        product.setProductName(updateProductRequestDto.getProductName());
        product.setCategory(updateProductRequestDto.getCategory());
        product.setPrice(updateProductRequestDto.getPrice());
        product.setDescription(updateProductRequestDto.getDescription());

        return new UpdateProductRequestDto(productRepository.update(product));
    }

    @Override
    public void deleteByProductId(UUID productId) {
        productRepository.deleteById(productId);
    }
}
