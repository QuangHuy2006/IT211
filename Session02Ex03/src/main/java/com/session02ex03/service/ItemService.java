package com.session02ex03.service;


import com.session02ex03.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemService {

    private final List<Item> items = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public ItemService() {
        items.add(new Item(nextId.getAndIncrement(), "Gạo ST25", 180000, 50));
        items.add(new Item(nextId.getAndIncrement(), "Nước mắm Nam Ngư", 45000, 100));
        items.add(new Item(nextId.getAndIncrement(), "Mì Hảo Hảo", 5000, 500));
    }

    public List<Item> getAllItems() {
        return items;
    }

    public Optional<Item> getItemById(Long id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public Item createItem(Item item) {
        item.setId(nextId.getAndIncrement());
        items.add(item);
        return item;
    }

    public Optional<Item> updateItem(Long id, Item newItemData) {
        Optional<Item> existingItem = getItemById(id);

        if (existingItem.isPresent()) {
            Item item = existingItem.get();
            item.setName(newItemData.getName());
            item.setPrice(newItemData.getPrice());
            item.setQuantity(newItemData.getQuantity());
            return Optional.of(item);
        }

        return Optional.empty();
    }

    public boolean deleteItem(Long id) {
        return items.removeIf(item -> item.getId().equals(id));
    }
}
