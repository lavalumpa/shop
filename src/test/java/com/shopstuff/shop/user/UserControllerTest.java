package com.shopstuff.shop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopstuff.shop.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
    @WithAnonymousUser
    public void testAddUser() throws Exception {
        var userDTO = UserDTO.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        var json = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/user/1"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
        var cart = cartRepository.findAll().get(0);
        var user= userRepository.findAll().get(0);
        assertEquals(cart.getUser().getId(), user.getId());
    }

    @Test
    public void testAddInvalidUser() throws Exception {
        var userDTO = UserDTO.builder().name("Steve").email("test").password("4az5j@98gbmawq").build();
        var json = objectMapper.writeValueAsString(userDTO);
        var error = mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
        assertTrue(error.contains("Email should be valid"));
    }
}
