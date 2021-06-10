package com.shopstuff.shop.cart;

import com.shopstuff.shop.item.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartDTOTest {

    @Test
    public void testingNoItemsInCart() {
        Cart cart = Cart.builder().id(1).build();
        var cartDTO = CartDTO.toDTO(cart);
        assertEquals(cart.getId(), cartDTO.getId());
        assertThat(cartDTO.getCartItems()).isEmpty();
    }

    @Test
    public void testingToDTOWithItemAndQuantityInCart() {
        Cart cart = Cart.builder().id(1).build();
        var item1 = Item.builder().id(1).price(50).build();
        var item2 = Item.builder().id(2).price(70).build();
        cart.addCartItem(CartItem.builder().item(item1).quantity(2).build());
        cart.addCartItem(CartItem.builder().item(item2).quantity(3).build());
        var cartDTO = CartDTO.toDTO(cart);
        assertEquals(1, cartDTO.getCartItems().get(0).getItemId());
        assertEquals(2, cartDTO.getCartItems().get(1).getItemId());
        assertEquals(2, cartDTO.getCartItems().get(0).getQuantity());
        assertEquals(3, cartDTO.getCartItems().get(1).getQuantity());
        assertEquals(2 * 50, cartDTO.getCartItems().get(0).getTotalPrice());
        assertEquals(3 * 70, cartDTO.getCartItems().get(1).getTotalPrice());
        assertEquals(1, cartDTO.getId());
        assertEquals(3 * 70 + 2 * 50, cartDTO.totalPrice());
    }
}
