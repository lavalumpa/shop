package com.shopstuff.shop.item;

import com.shopstuff.shop.exceptions.NameDuplicateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void testingItemExistsByName(){
        var item=createPhone();
        when(itemRepository.existsByName("Phone")).thenReturn(true);
        assertThrows(NameDuplicateException.class,()->itemService.saveItem(item));
    }

    @Test
    public void testingItemSaved(){
        var item=createPhone();
        when(itemRepository.existsByName("Phone")).thenReturn(false);
        itemService.saveItem(item);
        verify(itemRepository).save(item);
    }

    private Item createPhone(){
        return Item.builder()
                .name("Phone")
                .price(7000)
                .build();
    }
}
