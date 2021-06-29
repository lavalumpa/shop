package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.item.ItemDTO;
import com.shopstuff.shop.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ViewedItemController {

    private final ViewedItemService viewedItemService;
    private final UserService userService;

    @GetMapping("/user/{id}/items/recent")
    @PreAuthorize("hasRole('ADMIN') or (@userService.correctUser(principal.username,#id) and hasRole('CUSTOMER'))")
    public Page<ItemDTO> recentItemsByUser(@PathVariable int id,
                                           @PageableDefault(size = 5, sort = "lastViewed") Pageable pageable) {
        var page = viewedItemService.recentItemsByUser(id, pageable);
        return page.map(ItemDTO::toDto);
    }
}
