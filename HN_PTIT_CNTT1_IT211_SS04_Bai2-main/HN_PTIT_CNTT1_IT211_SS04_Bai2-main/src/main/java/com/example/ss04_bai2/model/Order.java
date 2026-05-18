package com.example.ss04_bai2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;              // ID đơn hàng - sinh bằng UUID
    private String customerName;         // Tên khách hàng
    private String address;              // Địa chỉ giao hàng
    private String productId;            // ID sản phẩm
    private int quantity;                // Số lượng đặt
    private LocalDateTime createdAt;     // Thời gian tạo đơn hàng
}

