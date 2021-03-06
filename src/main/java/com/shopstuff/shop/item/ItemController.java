package com.shopstuff.shop.item;


import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.viewed.ViewedItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.security.Principal;


@RestController
@RequestMapping("item")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ViewedItemService viewedItemService;


    @GetMapping("/{id}")
    public Item getItem(@PathVariable int id, Principal principal) {
        var item= itemService.findById(id).orElseThrow(NotFoundException::new);
        viewedItemService.itemViewed(principal.getName(),item);
        return item;
    }

    @GetMapping("/search")
    public Page<Item> searchItems(@RequestParam(name="q") String name,@PageableDefault(size=10) Pageable pageable){
        return itemService.searchByName(name,pageable);
    }

    @GetMapping
    public Page<Item> allItems(@PageableDefault(size = 20) Pageable pageable) {
        return itemService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item saved= itemService.saveItem(item);
        return ResponseEntity.created(URI.create("/item/"+ saved.getId())).body(item);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Item updateItem(@PathVariable int id, @RequestBody Item item) {
        if (itemService.existsById(id)) {
            item.setId(id);
        }
        return itemService.saveItem(item);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id){
        itemService.deleteById(id);
    }

}
