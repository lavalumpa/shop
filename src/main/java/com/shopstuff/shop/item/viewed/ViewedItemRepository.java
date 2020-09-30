package com.shopstuff.shop.item.viewed;

import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ViewedItemRepository extends JpaRepository<ViewedItem,Integer> {
    Page<ViewedItem> findByUser(User user, Pageable pageable);
    Optional<ViewedItem> findByUserAndItem(User user, Item item);
}
