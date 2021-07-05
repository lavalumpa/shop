package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
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
        mockMvc.perform(get("/user/{id}/items/recent", user.getId())
                .queryParam("size", "0").queryParam("page", "0"))
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()))
                .andDo(document("viewed-item/recent-items-by-user",
                        pathParameters(
                                parameterWithName("id").description("Id of the user who viewed items")
                        ),
                        requestParameters(
                                parameterWithName("size").description("Amount of items retrieved"),
                                parameterWithName("page").description("Page of the items retrieved")
                        ), relaxedResponseFields(
                                subsectionWithPath("content").description("Contains the items retrieved"),
                                fieldWithPath("content[].id").description("Id of one item"),
                                fieldWithPath("content[].name").description("Name of the item"),
                                fieldWithPath("content[].price").description("Price of the item")
                        )));
    }
}
