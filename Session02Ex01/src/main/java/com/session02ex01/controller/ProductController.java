package com.session02ex01.controller;

import com.session02ex01.model.Product;
import com.session02ex01.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/hot-products")
    public List<Product> getHotProducts() {
        return productService.getHotProducts();
    }
}
