package com.shopstuff.shop.item;


import com.shopstuff.shop.exceptions.NotFoundExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;


@RestController
@RequestMapping("item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public Item getItem(@PathVariable int id) {
        return itemService.findById(id).orElseThrow(NotFoundExceptions::new);
    }

    @GetMapping
    public Page<Item> allItems(@PageableDefault(size = 20) Pageable pageable) {
        return itemService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item saved= itemService.saveItem(item);
        return ResponseEntity.created(URI.create("/item/"+ saved.getId())).body(item);
    }

    @PutMapping("{id}")
    public Item updateItem(@PathVariable int id, @RequestBody Item item) {
        if (itemService.existsById(id)) {
            item.setId(id);
        }
        return itemService.saveItem(item);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id){
        itemService.deleteById(id);
    }

}
