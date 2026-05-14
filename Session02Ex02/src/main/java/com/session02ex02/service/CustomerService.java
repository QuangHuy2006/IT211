package com.session02ex02.service;


import com.session02ex02.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(nextId.getAndIncrement());
        customers.add(customer);
        return customer;
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    public Optional<Customer> updateCustomer(Long id, Customer newCustomerData) {
        Optional<Customer> existingCustomer = getCustomerById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setName(newCustomerData.getName());
            customer.setEmail(newCustomerData.getEmail());
            return Optional.of(customer);
        }

        return Optional.empty();
    }
}
