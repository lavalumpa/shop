package com.shopstuff.shop.item;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ItemControllerTest {


    private final MockMvc mockMvc;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @WithMockUser(username = "Steve")
    public void testGetItemWhenItemIsFoundWithGivenId() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        item = itemRepository.save(item);
        var user = User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        userRepository.save(user);
        mockMvc.perform(get("/item/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andDo(document("item/get-one-item",
                        pathParameters(
                                parameterWithName("id").description("The id of an item")),
                        responseFields(
                                fieldWithPath("id").description("Id of the item"),
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("price").description("Price of the item")
                        )));
    }

    private final ResponseFieldsSnippet responseFields = relaxedResponseFields(
            subsectionWithPath("content").description("Contains the items retrieved"),
            fieldWithPath("content[].id").description("Id of one item"),
            fieldWithPath("content[].name").description("Name of the item"),
            fieldWithPath("content[].price").description("Price of the item")
    );

    @Test
    @WithMockUser
    public void TestGetAllItemsWhenThereIsOneItem() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        item = itemRepository.save(item);
        mockMvc.perform(get("/item?page={page}&size={size}", 0, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()))
                .andDo(document("item/get-page", requestParameters(
                        parameterWithName("page").description("Page of the items retrieved, default is 0"),
                        parameterWithName("size").description("The amount of items per page, default is 20")
                ), this.responseFields));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testPostItemAndReturnBody() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        String json = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/item").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.price").value(item.getPrice()))
                .andDo(document("item/post-item",
                        requestFields(
                                fieldWithPath("id").description("Id of the item, not necessary as it will be provided"),
                                fieldWithPath("name").description("Name of the item to be added"),
                                fieldWithPath("price").description("Price of the item to be added")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of the newly created item"),
                                fieldWithPath("name").description("Name of the item that's going to be added"),
                                fieldWithPath("price").description("Price of the item that's going to be added")
                        ), responseHeaders(
                                headerWithName("Location").description("The location header of the newly created item")
                        )));
    }

    @Test
    @WithMockUser
    public void testSearchItemWithGivenName() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        item = itemRepository.save(item);
        mockMvc.perform(get("/item/search")
                .param("q", "pho")
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(item.getId()))
                .andExpect(jsonPath("$.content[0].name").value(item.getName()))
                .andExpect(jsonPath("$.content[0].price").value(item.getPrice()))
                .andDo(document("item/search",
                        requestParameters(
                                parameterWithName("q").description("Name or partial name of the item's name to be searched"),
                                parameterWithName("page").description("Page of the result, default 0"),
                                parameterWithName("size").description("The amount of items per page, default is 10")
                        ), this.responseFields));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteItem() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        item = itemRepository.save(item);
        mockMvc.perform(delete("/item/{id}", item.getId()).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(document("item/delete-item",
                        pathParameters(
                                parameterWithName("id").description("Id of the item to be deleted")
                        )));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testPutUpdatingItemChangingTheItem() throws Exception {
        Item item = Item.builder().name("Phone").price(7000).build();
        item = itemRepository.save(item);
        Item updatedItem = Item.builder().name("Adapter").price(2000).build();
        String json = objectMapper.writeValueAsString(updatedItem);
        mockMvc.perform(put("/item/{id}", item.getId()).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.price").value(updatedItem.getPrice()))
                .andExpect(jsonPath("$.name").value(updatedItem.getName()))
                .andDo(document("item/update",
                        pathParameters(
                                parameterWithName("id").description("Id of the item to be updated")
                        ),
                        requestFields(
                                fieldWithPath("id").description("Field is ignored as the id is provided by the path variable"),
                                fieldWithPath("name").description("New name of the item"),
                                fieldWithPath("price").description("New price of the item")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of the changed item"),
                                fieldWithPath("name").description("New name of the changed item"),
                                fieldWithPath("price").description("New price of the item")
                        )));
    }


}
