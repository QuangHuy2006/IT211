package com.example.ss04_bai4.service;

import com.example.ss04_bai4.model.Product;
import com.example.ss04_bai4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy danh sách tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Lấy sản phẩm theo ID
     */
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    /**
     * Tạo sản phẩm mới
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Cập nhật sản phẩm
     */
    public Optional<Product> updateProduct(Long productId, Product productDetails) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            if (productDetails.getProductName() != null) {
                product.setProductName(productDetails.getProductName());
            }
            if (productDetails.getPrice() != null) {
                product.setPrice(productDetails.getPrice());
            }
            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }
            if (productDetails.getQuantity() != null) {
                product.setQuantity(productDetails.getQuantity());
            }
            if (productDetails.getCategory() != null) {
                product.setCategory(productDetails.getCategory());
            }
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    /**
     * Xóa sản phẩm
     */
    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }
}

