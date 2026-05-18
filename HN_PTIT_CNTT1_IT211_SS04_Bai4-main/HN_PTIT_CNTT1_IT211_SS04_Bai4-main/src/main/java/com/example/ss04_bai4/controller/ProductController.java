package com.example.ss04_bai4.controller;

import com.example.ss04_bai4.model.Product;
import com.example.ss04_bai4.dto.ApiResponse;
import com.example.ss04_bai4.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * GET /api/v1/products
     * Lấy danh sách tất cả sản phẩm
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(
            ApiResponse.<List<Product>>builder()
                .success(true)
                .message("Lấy danh sách sản phẩm thành công")
                .data(products)
                .build()
        );
    }

    /**
     * GET /api/v1/products/{productId}
     * Lấy sản phẩm theo ID
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        if (product.isPresent()) {
            return ResponseEntity.ok(
                ApiResponse.<Product>builder()
                    .success(true)
                    .message("Lấy sản phẩm thành công")
                    .data(product.get())
                    .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Product>builder()
                    .success(false)
                    .message("Sản phẩm với ID " + productId + " không tồn tại")
                    .data(null)
                    .build()
                );
        }
    }

    /**
     * POST /api/v1/products
     * Tạo sản phẩm mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<Product>builder()
                .success(true)
                .message("Tạo sản phẩm thành công")
                .data(createdProduct)
                .build()
            );
    }

    /**
     * PUT /api/v1/products/{productId}
     * Cập nhật toàn bộ thông tin sản phẩm
     *
     * ✓ ĐÚNG: Sử dụng @PutMapping (không phải @PostMapping)
     * ✓ ĐÚNG: Trả về ResponseEntity với status code phù hợp
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody Product productDetails) {

        Optional<Product> updatedProduct = productService.updateProduct(productId, productDetails);

        if (updatedProduct.isPresent()) {
            return ResponseEntity.ok(
                ApiResponse.<Product>builder()
                    .success(true)
                    .message("Cập nhật sản phẩm thành công")
                    .data(updatedProduct.get())
                    .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Product>builder()
                    .success(false)
                    .message("Sản phẩm với ID " + productId + " không tồn tại, không thể cập nhật")
                    .data(null)
                    .build()
                );
        }
    }

    /**
     * DELETE /api/v1/products/{productId}
     * Xóa sản phẩm theo ID
     *
     * ✓ ĐÚNG: Sử dụng @DeleteMapping
     * ✓ ĐÚNG: Trả về thông báo xác nhận (không trả về body rỗng)
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        boolean isDeleted = productService.deleteProduct(productId);

        if (isDeleted) {
            return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                    .success(true)
                    .message("Xóa sản phẩm ID " + productId + " thành công")
                    .data(null)
                    .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                    .success(false)
                    .message("Sản phẩm với ID " + productId + " không tồn tại, không thể xóa")
                    .data(null)
                    .build()
                );
        }
    }
}

