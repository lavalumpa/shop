package com.shopstuff.shop.item;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@AutoConfigureTestDatabase
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Test
    @WithMockUser(username = "Steve")
    public void testGetItemWhenItemIsFoundWithGivenId() throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        var user=User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        user=userRepository.save(user);
        mockMvc.perform(get("/item/{id}",item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()));
    }

    @Test
    @WithMockUser
    public void TestGetAllItemsWhenThereIsOneItem() throws Exception{
        Item item= Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        mockMvc.perform(get("/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testPostItemAndReturnBody() throws Exception{
        Item item= Item.builder().name("Phone").price(7000).build();
        String json= objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/item").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()));
    }

    @Test
    @WithMockUser
    public void testSearchItemWithGivenName() throws Exception{
        Item item= Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        mockMvc.perform(get("/item/search").param("q","pho"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteItem() throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        mockMvc.perform(delete("/item/{id}", item.getId()).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testPutUpdatingItemChangingTheItem()throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        Item updatedItem=Item.builder().name("Adapter").price(2000).build();;
        String json= objectMapper.writeValueAsString(updatedItem);
        mockMvc.perform(put("/item/{id}",item.getId()).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.price").value(updatedItem.getPrice()))
                .andExpect(jsonPath("$.name").value(updatedItem.getName()));
    }


}
