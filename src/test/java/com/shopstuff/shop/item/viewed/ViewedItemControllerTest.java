package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ViewedItemControllerTest {

    private final MockMvc mockMvc;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ViewedItemRepository viewedItemRepository;

    @Test
    @WithMockUser(username = "Steve", roles = "CUSTOMER")
    public void testRecentItemsByUserWithOneItemChecked() throws Exception {
        var user = User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        user = userRepository.save(user);
        var item = Item.builder().price(700).name("phone").build();
        item = itemRepository.save(item);
        var viewedItem = ViewedItem.builder().user(user).item(item).build();
        viewedItem = viewedItemRepository.save(viewedItem);
        mockMvc.perform(get("/user/{id}/items/recent", user.getId()))
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()));
    }


}
