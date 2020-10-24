package com.shopstuff.shop.item.viewed;

import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ViewedServiceTest {
    @InjectMocks
    private ViewedItemService viewedItemService;

    @Mock
    private UserService userService;

    @Mock
    private ViewedItemRepository viewedItemRepository;

    @Captor
    private ArgumentCaptor<ViewedItem> captor;

    @Test
    public void testRecentItemsWhenUserIsNotFound() {
        when(userService.findById(eq(1))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> viewedItemService.recentItemsByUser(1,
                        PageRequest.of(0, 5, Sort.by("lastViewed").descending())));
    }

    @Test
    public void testItemViewed() {
        when(userService.findByName(eq("steve"))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> viewedItemService.itemViewed("steve", Item.builder().build()));
    }

    @Test
    public void testViewedItemWhenItemWasNotViewedByUser() {
        var user=User.builder().id(1).name("steve").roles(Set.of(Role.CUSTOMER)).build();
        when(userService.findByName(eq("steve"))).thenReturn(Optional.of(user));
        var item=Item.builder().id(1).name("phone").price(700).build();
        when(viewedItemRepository.findByUserAndItem(eq(user),eq(item))).thenReturn(Optional.empty());
        viewedItemService.itemViewed("steve",item);
        verify(viewedItemRepository).save(captor.capture());
        assertEquals(captor.getValue().getItem(),item);
        assertEquals(captor.getValue().getUser(),user);
    }


    @Test
    public void testViewedItemWhenItemWasViewedByUser() {
        var user=User.builder().id(1).name("steve").roles(Set.of(Role.CUSTOMER)).build();
        when(userService.findByName(eq("steve"))).thenReturn(Optional.of(user));
        var item=Item.builder().id(1).name("phone").price(700).build();
        var viewedItem=ViewedItem.builder().user(user).item(item).build();
        when(viewedItemRepository.findByUserAndItem(eq(user),eq(item))).thenReturn(Optional.of(viewedItem));
        viewedItemService.itemViewed("steve",item);
        verify(viewedItemRepository).save(captor.capture());
        assertEquals(captor.getValue().getItem(),item);
        assertEquals(captor.getValue().getUser(),user);
    }

    @Test
    public void testNoItemsViewedByUser(){
        var user=User.builder().id(1).build();
        var page=PageRequest.of(0, 5, Sort.by("lastViewed").descending());
        when(userService.findById(eq(1))).thenReturn(Optional.of(user));
        when(viewedItemRepository.findByUser(eq(user),eq(page))).thenReturn(Page.empty());
        assertTrue(viewedItemService.recentItemsByUser(1,page).isEmpty());
    }

    @Test
    public void testTwoItemsViewedByUser(){
        var user=User.builder().id(1).build();
        var page=PageRequest.of(0, 5, Sort.by("lastViewed").descending());
        when(userService.findById(eq(1))).thenReturn(Optional.of(user));
        var item1=Item.builder().id(1).name("phone").price(700).build();
        var item2=Item.builder().id(1).name("phone").price(700).build();
        var viewedItem1=ViewedItem.builder().item(item1)
                .lastViewed(LocalDateTime.of(1999, Month.APRIL,17,17,17)).user(user).build();
        var viewedItem2=ViewedItem.builder().item(item2)
                .lastViewed(LocalDateTime.of(1999, Month.APRIL,18,10,3)).user(user).build();
        when(viewedItemRepository.findByUser(eq(user),eq(page)))
                .thenReturn(new PageImpl<>(List.of(viewedItem1, viewedItem2)));
        var pageOfItems=viewedItemService.recentItemsByUser(1,page);
        assertEquals(pageOfItems.toList().get(0),item2);
        assertEquals(pageOfItems.toList().get(1),item1);
    }
}
