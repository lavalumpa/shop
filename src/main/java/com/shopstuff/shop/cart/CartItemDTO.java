package com.shopstuff.shop.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    @JsonProperty("id")
    private int itemId;
    private int quantity;
    private int totalPrice;

    public static CartItemDTO toDTO(CartItem cartItem) {
        var dto = CartItemDTO.builder()
                .itemId(cartItem.getItem().getId())
                .quantity(cartItem.getQuantity())
                .build();
        dto.setTotalPrice(cartItem.getItem().getPrice() * cartItem.getQuantity());
        return dto;
    }


}
