package com.example.coffeestore.product.controller.api;

import com.example.coffeestore.product.controller.dto.CreateProductRequestDto;
import com.example.coffeestore.product.controller.dto.ProductResponseDto;
import com.example.coffeestore.product.domain.Category;
import com.example.coffeestore.product.service.ProductService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductResponseDto> productList(@RequestParam Optional<Category> category) {
        return category
            .map(i -> productService.getProductsByCategory(i))
            .orElse(productService.getAllProducts());
    }

    @PostMapping("/products")
    public Object addProduct(@RequestBody @Validated CreateProductRequestDto productRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 error={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        return productService.createProduct(productRequestDto);
    }
}
