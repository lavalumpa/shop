package com.shopstuff.shop.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.delivery.Address;
import com.shopstuff.shop.delivery.DeliveryDTO;
import com.shopstuff.shop.delivery.DeliveryRepository;
import com.shopstuff.shop.delivery.weather.OpenWeatherAPI;
import com.shopstuff.shop.delivery.weather.OpenWeatherProps;
import com.shopstuff.shop.delivery.weather.WeatherDTO;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    private final DeliveryRepository deliveryRepository;
    private final OpenWeatherProps openWeatherProps;

    @MockBean
    private OpenWeatherAPI openWeatherAPI;




    @Test
    @WithMockUser(roles = "CUSTOMER", username = "steve")
    public void testingShowCart() throws Exception{
        var cart = Cart.builder().user(User.builder().name("steve").build()).build();
        var item = Item.builder().name("Phone").price(7000).build();
        item=itemRepository.save(item);
        cart.addCartItem(CartItem.builder().item(item).quantity(2).build());
        cartRepository.save(cart);
        mockMvc.perform(get("/cart/{id}",cart.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalPrice").value(7000*2))
                .andExpect(jsonPath("$.cartItems[0].id").value(item.getId()))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.cartItems[0].totalPrice").value(item.getPrice()*2));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "steve")
    public void testingAddItemToCart() throws Exception{
        var cart = Cart.builder().user(User.builder().name("steve").build()).build();
        cartRepository.save(cart);
        var cartItemDto=CartItemDTO.builder().itemId(1).quantity(5).build();
        var item = Item.builder().name("Phone").price(2_000).build();
        itemRepository.save(item);
        String json = objectMapper.writeValueAsString(cartItemDto);
        mockMvc.perform(post("/cart/{id}/item",cart.getId()).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalPrice").value(10_000))
                .andExpect(jsonPath("$.cartItems[0].totalPrice").value(item.getPrice()*5))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(cartItemDto.getQuantity()))
                .andExpect(jsonPath("$.cartItems[0].id").value(cartItemDto.getItemId()));

    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "steve")
    public void testingPurchaseWithTwoItemsWithNoDelivery() throws Exception {
        var cart = Cart.builder().build();
        var role=Role.valueOf("CUSTOMER");
        var user=User.builder().name("steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").roles(Set.of(role)).build();
        user=userRepository.save(user);
        cart.setUser(user);
        var item1=Item.builder().name("Phone").price(1000).build();
        var item2=Item.builder().name("Headphones").price(400).build();
        item1=itemRepository.save(item1);
        item2=itemRepository.save(item2);
        cart.addCartItem(CartItem.builder().item(item1).quantity(2).build());
        cart.addCartItem(CartItem.builder().item(item2).quantity(5).build());
        cartRepository.save(cart);
        mockMvc.perform(post("/cart/{id}/purchase",cart.getId()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"deliver\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.items[0].id").value(item1.getId()))
                .andExpect(jsonPath("$.items[0].price").value(1000))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[1].id").value(item2.getId()))
                .andExpect(jsonPath("$.items[1].price").value(400))
                .andExpect(jsonPath("$.items[1].quantity").value(5))
                .andExpect(jsonPath("$.purchasedAt").exists())
                .andExpect(jsonPath("$.purchasedBy").value(user.getId()))
                .andExpect(jsonPath("$.totalPrice").value(1000*2+5*400));
        assertTrue(deliveryRepository.findAll().isEmpty());
        assertEquals(0,cart.getCartItems().size());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "steve")
    public void testingPurchaseWithTwoItemsWitDelivery() throws Exception {
        var cart = Cart.builder().build();
        var role = Role.valueOf("CUSTOMER");
        var user = User.builder().name("steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").roles(Set.of(role)).build();
        user=userRepository.save(user);
        cart.setUser(user);
        var item1 = Item.builder().name("Phone").price(1000).build();
        var item2 = Item.builder().name("Headphones").price(400).build();
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        cart.addCartItem(CartItem.builder().item(item1).quantity(2).build());
        cart.addCartItem(CartItem.builder().item(item2).quantity(5).build());
        cartRepository.save(cart);
        var weatherDTO=new WeatherDTO();
        var weather=new WeatherDTO.WeatherInfoDTO.Weather();
        weather.setMain("Rain");
        var weatherInfo= new WeatherDTO.WeatherInfoDTO();
        weatherInfo.setWeather(List.of(weather));
        weatherInfo.setForecastTime(LocalDateTime.now());
        weatherDTO.setWeatherInfoList(List.of(weatherInfo));
        var deliveryDTO = DeliveryDTO.builder().deliveryRequested(true)
                .address(Address.builder()
                        .city("Belgrade")
                        .number(17)
                        .street("Sazonova").build()).build();
        String json=objectMapper.writeValueAsString(deliveryDTO);
        when(openWeatherAPI.cityDayForecast("Belgrade",8,openWeatherProps.getKeyValue())).thenReturn(weatherDTO);
        mockMvc.perform(post("/cart/{id}/purchase", cart.getId()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.items[0].id").value(item1.getId()))
                .andExpect(jsonPath("$.items[0].price").value(1000))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[1].id").value(item2.getId()))
                .andExpect(jsonPath("$.items[1].price").value(400))
                .andExpect(jsonPath("$.items[1].quantity").value(5))
                .andExpect(jsonPath("$.purchasedAt").exists())
                .andExpect(jsonPath("$.purchasedBy").value(user.getId()))
                .andExpect(jsonPath("$.totalPrice").value(1000 * 2 + 5 * 400));
        var delivery=deliveryRepository.findAll().get(0);
        assertThat("Belgrade").isEqualTo(delivery.getCity());
        assertThat("Sazonova").isEqualToIgnoringCase(delivery.getStreet());
        assertThat(17).isEqualTo(delivery.getNumber());
        assertThat(LocalDate.now().plusDays(2)).isEqualTo(delivery.getEstimatedDate());
        assertTrue(cart.getCartItems().isEmpty());
    }

}
