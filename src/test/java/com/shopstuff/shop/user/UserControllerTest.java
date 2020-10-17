package com.shopstuff.shop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    @Test
    public void testAddUser() throws Exception {
        var user = User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        var json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/user/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists());
        var cart = cartRepository.findAll().get(0);
        user = userRepository.findAll().get(0);
        assertEquals(cart.getUser().getId(), user.getId());
    }

    @Test
    public void testAddInvalidUser() throws Exception {
        var user = User.builder().name("Steve").email("test").password("4az5j@98gbmawq").build();
        var json = objectMapper.writeValueAsString(user);
        var error = mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
        assertTrue(error.contains("Email should be valid"));
    }
}
