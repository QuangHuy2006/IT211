package com.example.ss04_bai4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;

    private String description;

    @Positive(message = "Số lượng trong kho phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    private String category;
}

