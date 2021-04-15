package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ViewedItemService {
    private final UserService userService;
    private final ViewedItemRepository viewedItemRepository;

    @Transactional
    public void itemViewed(String name, Item item) {
        var user = userService.findByName(name).orElseThrow(NotFoundException::new);
        if (!user.getRoles().contains(Role.CUSTOMER)) {
            return;
        }
        viewedItemRepository.findByUserAndItem(user, item).ifPresentOrElse(
                x -> {
                    x.setLastViewed(LocalDateTime.now());
                    viewedItemRepository.save(x);
                }, () -> {
                    var viewedItem = ViewedItem.builder().item(item).user(user).lastViewed(LocalDateTime.now()).build();
                    viewedItemRepository.save(viewedItem);
                }
        );
    }

    @Transactional
    public Page<Item> recentItemsByUser(int id, Pageable pageable) {
        var user = userService.findById(id).orElseThrow(NotFoundException::new);
        var page = viewedItemRepository.findByUser(user, pageable);
        return page.map(ViewedItem::getItem);
    }


}
