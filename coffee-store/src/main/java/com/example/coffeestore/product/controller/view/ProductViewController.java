package com.example.coffeestore.product.controller.view;

import com.example.coffeestore.product.controller.dto.CreateProductRequestDto;
import com.example.coffeestore.product.controller.dto.ProductResponseDto;
import com.example.coffeestore.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final ProductService productService;

    @GetMapping("/products")
    public String productsPage(Model model) {
        List<ProductResponseDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/new-product")
    public String newProductPage(Model model) {
        model.addAttribute("product", new CreateProductRequestDto());
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(@Validated @ModelAttribute("product") CreateProductRequestDto createProductRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "new-product";
        }
        productService.createProduct(createProductRequestDto);
        return "redirect:/products";
    }

}
