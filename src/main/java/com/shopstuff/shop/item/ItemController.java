package com.shopstuff.shop.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public Item getItem(@PathVariable int id) {
        return itemService.findById(id).orElseThrow(NotFoundException::new);
    }
    @GetMapping(params="series")
    public Item itemBySeries(@RequestParam String series) {
        return itemService.findBySeries(series).orElseThrow(NotFoundException::new);
    }

    @GetMapping
    public List<Item> allItems(@PageableDefault(size = 20) Pageable pageable) {
        return itemService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFoundException extends RuntimeException {}

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        if (!itemService.existsBySeries(item.getSeries())){
            itemService.saveOrUpdate(item);
            return ResponseEntity.created(URI.create("/items" + item.getId())).body(item);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("{series}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemBySeries(@PathVariable String series){
        itemService.deleteBySeries(series);
    }

    @PutMapping("{id}")
    public Item updateItem(@PathVariable int id ,@RequestBody Item item){
        if (itemService.existsById(id)){
            item.setId(id);
        }
        return itemService.saveOrUpdate(item);
    }
}
