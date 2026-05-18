package com.example.ss04_bai4.controller;

import com.example.ss04_bai4.model.Product;
import com.example.ss04_bai4.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    public void setup() {
        testProduct = new Product();
        testProduct.setProductName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setDescription("Test Description");
        testProduct.setQuantity(100);
        testProduct.setCategory("Test Category");
    }

    /**
     * Test GET /api/v1/products - Lấy danh sách sản phẩm
     */
    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lấy danh sách sản phẩm thành công"))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * Test POST /api/v1/products - Tạo sản phẩm mới
     */
    @Test
    public void testCreateProduct() throws Exception {
        String productJson = objectMapper.writeValueAsString(testProduct);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tạo sản phẩm thành công"))
                .andExpect(jsonPath("$.data.productName").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(99.99));
    }

    /**
     * Test PUT /api/v1/products/{productId} - Cập nhật sản phẩm
     */
    @Test
    public void testUpdateProduct() throws Exception {
        // Tạo sản phẩm trước
        Product created = productService.createProduct(testProduct);
        Long productId = created.getProductId();

        // Cập nhật sản phẩm
        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setPrice(199.99);
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setQuantity(50);
        updatedProduct.setCategory("Updated Category");

        String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(put("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cập nhật sản phẩm thành công"))
                .andExpect(jsonPath("$.data.productName").value("Updated Product"))
                .andExpect(jsonPath("$.data.price").value(199.99));
    }

    /**
     * Test PUT /api/v1/products/{productId} - ID không tồn tại
     */
    @Test
    public void testUpdateProductNotFound() throws Exception {
        Long nonExistentId = 9999L;

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setPrice(199.99);

        String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);

        mockMvc.perform(put("/api/v1/products/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProductJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").containsString("không tồn tại"));
    }

    /**
     * Test DELETE /api/v1/products/{productId} - Xóa sản phẩm
     */
    @Test
    public void testDeleteProduct() throws Exception {
        // Tạo sản phẩm trước
        Product created = productService.createProduct(testProduct);
        Long productId = created.getProductId();

        // Xóa sản phẩm
        mockMvc.perform(delete("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").containsString("thành công"));

        // Verify sản phẩm đã bị xóa
        Optional<Product> deleted = productService.getProductById(productId);
        assert !deleted.isPresent();
    }

    /**
     * Test DELETE /api/v1/products/{productId} - ID không tồn tại
     */
    @Test
    public void testDeleteProductNotFound() throws Exception {
        Long nonExistentId = 9999L;

        mockMvc.perform(delete("/api/v1/products/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").containsString("không tồn tại"));
    }

    /**
     * Test GET /api/v1/products/{productId} - ID không tồn tại
     */
    @Test
    public void testGetProductNotFound() throws Exception {
        Long nonExistentId = 9999L;

        mockMvc.perform(get("/api/v1/products/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").containsString("không tồn tại"));
    }
}

