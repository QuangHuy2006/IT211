package com.example.ss04_bai2.controller;

import com.example.ss04_bai2.model.Order;
import com.example.ss04_bai2.dto.CreateOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {


    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        // Sinh orderId tự động bằng UUID
        String orderId = UUID.randomUUID().toString();
        
        // Ghi nhận thời gian tạo đơn hàng
        LocalDateTime createdAt = LocalDateTime.now();
        
        // Tạo đối tượng Order với đầy đủ thông tin
        Order order = new Order(
            orderId,
            createOrderRequest.getCustomerName(),
            createOrderRequest.getAddress(),
            createOrderRequest.getProductId(),
            createOrderRequest.getQuantity(),
            createdAt
        );
        
        // Trả về HTTP 201 Created với đối tượng Order đã tạo
        // Client sẽ nhận được đối tượng Order hoàn chỉnh kèm orderId
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
