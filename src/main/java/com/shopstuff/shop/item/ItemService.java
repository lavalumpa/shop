package com.shopstuff.shop.item;

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

    public boolean existsById(int id){
        return itemRepository.existsById(id);
    }

    public Item saveOrUpdate(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> findById(int id){
        return itemRepository.findById(id);
    }

    public Page<Item> findAll(Pageable pageable){
        return itemRepository.findAll(pageable);
    }

}
