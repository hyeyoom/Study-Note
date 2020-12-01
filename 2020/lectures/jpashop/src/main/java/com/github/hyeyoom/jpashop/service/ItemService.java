package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.item.Item;
import com.github.hyeyoom.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    @Transactional
    public void saveItem(Item item) {
        repository.save(item);
    }

    public List<Item> findItems() {
        return repository.findAll();
    }

    public Item findOne(Long itemId) {
        return repository.findOne(itemId);
    }
}
