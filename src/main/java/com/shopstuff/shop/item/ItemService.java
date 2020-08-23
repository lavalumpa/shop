package com.shopstuff.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
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

    public boolean existsBySeries (String series){
        return itemRepository.existsBySeries();
    }


    public Item saveOrUpdate(Item item) {
        return itemRepository.save(item));
    }

    public Optional<Item> findById(int id){
        return itemRepository.findById(id);
    }

    public void deleteBySeries (int series){
        return itemRepository.deleteBySeries(series);
    }

    public List<Item> findAll(Pageable pageable){
        return itemRepository.findAll();
    }

    public Optional<Item> findBySeries (String series){
        return itemRepository.findBySeries(series);
    }
}
