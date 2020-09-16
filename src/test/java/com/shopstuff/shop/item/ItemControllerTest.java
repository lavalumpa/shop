package com.shopstuff.shop.item;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Test
    public void testGetItemWhenItemIsFound() throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        itemRepository.save(item);
        mockMvc.perform(get("/item/{id}",item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()));
    }

    @Test
    public void testPostItemAndReturnBody() throws Exception{
        Item item= Item.builder().name("Phone").price(7000).build();
        item.setId(1);
        String json= objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/item").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/item/1"))
                .andExpect(content().string(json));
    }

    @Test
    public void testDeleteItem() throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        itemRepository.save(item);
        mockMvc.perform(delete("/item/{id}", item.getId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    public void testPutUpdatingItemChangingTheItem()throws Exception {
        Item item= Item.builder().name("Phone").price(7000).build();
        itemRepository.save(item);
        Item updatedItem=Item.builder().name("Adapter").price(2000).build();;
        String json= objectMapper.writeValueAsString(updatedItem);
        mockMvc.perform(put("/item/{1}",item.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.price").value(updatedItem.getPrice()))
                .andExpect(jsonPath("$.name").value(updatedItem.getName()));
    }

}
