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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ItemDTO getItem(@PathVariable int id, Principal principal) {
        var item = itemService.findById(id).orElseThrow(NotFoundException::new);
        if (principal!=null){
            viewedItemService.itemViewed(principal.getName(), item);
        }
        return ItemDTO.toDto(item);
    }

    @GetMapping("/search")
    public Page<ItemDTO> searchItems(@RequestParam(name = "q") String name, @PageableDefault(size = 10) Pageable pageable) {
        var itemPage=itemService.searchByName(name, pageable);
        return itemPage.map(ItemDTO::toDto);
    }

    @GetMapping
    public Page<ItemDTO> allItems(@PageableDefault(size = 20) Pageable pageable) {
        return itemService.findAll(pageable).map(ItemDTO::toDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDTO> addItem(@RequestBody @Valid ItemDTO itemDTO) {
        var item=Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .build();
        var saved = itemService.saveItem(item);
        return ResponseEntity.created(URI.create("/item/" + saved.getId())).body(ItemDTO.toDto(saved));
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ItemDTO updateItem(@PathVariable int id, @RequestBody @Valid ItemDTO itemDTO) {
        var item=Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .build();
        if (itemService.existsById(id)) {
            item.setId(id);
        }
        var updated=itemService.saveItem(item);
        return ItemDTO.toDto(updated);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int id) {
        itemService.deleteById(id);
    }

}
