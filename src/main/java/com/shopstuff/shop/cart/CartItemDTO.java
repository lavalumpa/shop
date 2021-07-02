package com.shopstuff.shop.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class CartItemDTO {
    @JsonProperty("id")
    private int itemId;
    @Min(value = 1, message = "Quantity has to be greater than 1")
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
