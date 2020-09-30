package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ViewedItemController {

    private final ViewedItemService viewedItemService;

    @GetMapping("/user/{id}/items/recent")
    public Page<Item> recentItemsByUser(@PathVariable int id,
                                        @PageableDefault(size = 5, sort = "lastViewed") Pageable pageable) {
        return viewedItemService.recentItemsByUser(id, pageable);
    }
}
