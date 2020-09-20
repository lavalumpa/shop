package com.shopstuff.shop.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class CartControllerTest {

    private final MockMvc mockMvc;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    public void testingShowCart() throws Exception{
        var cart = Cart.builder().build();
        var item = Item.builder().id(1).name("Phone").price(7000).build();
        cart.addCartItem(CartItem.builder().item(item).quantity(2).build());
        cartRepository.save(cart);
        mockMvc.perform(get("/cart/{id}",cart.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(7000*2))
                .andExpect(jsonPath("$.cartItems[0].id").value(1))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.cartItems[0].totalPrice").value(item.getPrice()*2));
    }

    @Test
    public void testingAddItemToCart() throws Exception{
        var cart = Cart.builder().build();
        cartRepository.save(cart);
        var cartItemDto=CartItemDTO.builder().itemId(1).quantity(5).build();
        var item = Item.builder().name("Phone").price(2_000).build();
        itemRepository.save(item);
        String json = objectMapper.writeValueAsString(cartItemDto);
        mockMvc.perform(post("/cart/{id}/item",cart.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(10_000))
                .andExpect(jsonPath("$.cartItems[0].totalPrice").value(item.getPrice()*5))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(cartItemDto.getQuantity()))
                .andExpect(jsonPath("$.cartItems[0].id").value(cartItemDto.getItemId()));

    }

    @Test
    public void testingPurchaseWithTwoItems() throws Exception {
        var cart = Cart.builder().build();
        var user=User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        userRepository.save(user);
        cart.setUser(user);
        var item1=Item.builder().id(1).name("Phone").price(1000).build();
        var item2=Item.builder().id(2).name("Headphones").price(400).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        cart.addCartItem(CartItem.builder().item(item1).quantity(2).build());
        cart.addCartItem(CartItem.builder().item(item2).quantity(5).build());
        cartRepository.save(cart);
        mockMvc.perform(post("/cart/{id}/purchase",cart.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[0].price").value(1000))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[1].id").value(2))
                .andExpect(jsonPath("$.items[1].price").value(400))
                .andExpect(jsonPath("$.items[1].quantity").value(5))
                .andExpect(jsonPath("$.purchasedAt").exists())
                .andExpect(jsonPath("$.purchasedBy").value(1))
                .andExpect(jsonPath("$.totalPrice").value(1000*2+5*400));
        assertEquals(0,cart.getCartItems().size());
    }

}
