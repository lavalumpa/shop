package com.shopstuff.shop.item;

import com.shopstuff.shop.exceptions.NameDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    public Optional<Item> getItem(int id) {
        return itemRepository.findById(id);
    }

    public boolean existsById(int id) {
        return itemRepository.existsById(id);
    }

    public Item saveItem(Item item) {
        if (itemRepository.existsByName(item.getName())) throw new NameDuplicateException();
        return itemRepository.save(item);
    }

    public Page<Item> searchByName(String name, Pageable pageable) {
        return itemRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<Item> findById(int id) {
        return itemRepository.findById(id);
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public void deleteById(int id) {
        itemRepository.deleteById(id);
    }

}
