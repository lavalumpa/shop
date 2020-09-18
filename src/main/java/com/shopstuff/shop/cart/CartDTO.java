package com.shopstuff.shop.cart;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CartDTO {
    private int id;
    private List<CartItemDTO> cartItems;

    @JsonGetter
    public int totalPrice() {
        return cartItems.stream().map(CartItemDTO::getTotalPrice).reduce(Integer::sum).orElse(0);
    }

    public static CartDTO toDTO(Cart cart) {
        CartDTO cartDTO = CartDTO.builder().build();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cart.getCartItems()
                .stream()
                .map(CartItemDTO::toDTO)
                .collect(Collectors.toList()));
        return cartDTO;
    }

}
