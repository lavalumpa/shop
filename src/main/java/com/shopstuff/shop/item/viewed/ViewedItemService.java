package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ViewedItemService {
    private final UserService userService;
    private final ViewedItemRepository viewedItemRepository;

    public void itemViewed(int userId, Item item) {
        var user = userService.findById(userId).orElseThrow(NotFoundException::new);
        viewedItemRepository.findByUserAndItem(user, item).ifPresentOrElse(
                viewedItemRepository::save, () -> {
                    var viewedItem = ViewedItem.builder().item(item).user(user).build();
                    viewedItemRepository.save(viewedItem);
                }
        );
    }

    public Page<Item> recentItemsByUser(int id, Pageable pageable) {
        var user = userService.findById(id).orElseThrow(NotFoundException::new);
        var page = viewedItemRepository.findByUser(user, pageable);
        return page.map(ViewedItem::getItem);
    }


}
