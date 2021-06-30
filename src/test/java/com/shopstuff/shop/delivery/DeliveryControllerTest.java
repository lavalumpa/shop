package com.shopstuff.shop.delivery;

import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DeliveryControllerTest {


    private final MockMvc mockMvc;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    @Test
    @WithMockUser(roles = {"CUSTOMER"}, username = "steve")
    public void testingGettingDeliveryStatus() throws Exception {
        var user = User.builder().name("steve")
                .password("password")
                .email("generic@google.com").build();
        user.addRole(Role.CUSTOMER);
        user = userRepository.save(user);
        var delivery = Delivery.builder().city("Belgrade")
                .street("Sazonova")
                .number(17)
                .user(user)
                .deliveryState(DeliveryState.NOT_PROCESSED)
                .estimatedDate(LocalDate.now().plusDays(1))
                .build();
        delivery = deliveryRepository.save(delivery);
        mockMvc.perform(get("/delivery/{id}", delivery.getId()))
                .andExpect(jsonPath("$.address.city").value(delivery.getCity()))
                .andExpect(jsonPath("$.address.street").value(delivery.getStreet()))
                .andExpect(jsonPath("$.address.number").value(delivery.getNumber()))
                .andExpect(jsonPath("$.user_id").value(delivery.getUser().getId()))
                .andExpect(jsonPath("$.state").value(delivery.getDeliveryState().toString()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.estimated_date")
                        .value(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                .andExpect(jsonPath("$.delivered_at").isEmpty());
    }

    @Test
    @WithMockUser(roles = "DELIVERY_MANAGER")
    public void testingChangingDeliveryStatus() throws Exception {
        var user = User.builder().name("steve")
                .password("password")
                .email("generic@google.com").build();
        user.addRole(Role.CUSTOMER);
        user = userRepository.save(user);
        var delivery = Delivery.builder().city("Belgrade")
                .street("Sazonova")
                .number(17)
                .user(user)
                .estimatedDate(LocalDate.now().plusDays(1))
                .deliveryState(DeliveryState.NOT_PROCESSED)
                .build();
        delivery = deliveryRepository.save(delivery);
        mockMvc.perform(patch("/delivery/{id}", delivery.getId()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"state\":\"DELIVERED\"}"))
                .andExpect(jsonPath("$.state").value("DELIVERED"))
                .andExpect(jsonPath("$.address.city").value(delivery.getCity()))
                .andExpect(jsonPath("$.delivered_at").isNotEmpty());
    }


}
