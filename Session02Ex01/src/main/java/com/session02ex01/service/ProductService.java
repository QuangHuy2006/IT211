package com.session02ex01.service;

import com.session02ex01.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public List<Product> getHotProducts() {
        List<Product> products = new ArrayList<>();

        products.add(new Product("HP001", "Áo thun 'Code is Life'", 199000));
        products.add(new Product("HP002", "Móc khóa 'Bug Free'", 99000));
        products.add(new Product("HP003", "Cốc lập trình viên Java", 149000));
        products.add(new Product("HP004", "Sticker Debug Everyday", 49000));

        return products;
    }
}