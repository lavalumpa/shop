package com.shopstuff.shop.cart;


import com.shopstuff.shop.exceptions.BadRequestException;
import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemService;
import com.shopstuff.shop.receipt.Receipt;
import com.shopstuff.shop.receipt.ReceiptService;
import com.shopstuff.shop.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    @Mock
    private ReceiptService receiptService;

    @Mock
    private ItemService itemService;

    @Mock
    private CartRepository cartRepository;


    @Test
    public void testGetTotalPriceItemWithTwoItems() {
        Cart cart = Cart.builder().id(1).build();
        cart.getCartItems().add(CartItem.builder().item(Item.builder().price(200).build()).quantity(5).build());
        cart.getCartItems().add(CartItem.builder().item(Item.builder().price(300).build()).quantity(5).build());
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        assertEquals(300 * 5 + 200 * 5, cartService.totalPrice(1));
    }

    @Test
    public void testEmptyCart() {
        Cart cart = Cart.builder().id(1).build();
        when(cartRepository.findById(eq(1))).thenReturn((Optional.of(cart)));
        assertEquals(0, cartService.totalPrice(1));
    }

    @Test
    public void testGetTotalPriceItemAddingTwoRemovingOne() {
        Cart cart = Cart.builder().id(1).build();
        CartItem cartItem = CartItem.builder().item(Item.builder().price(100).build()).quantity(5).build();
        cart.getCartItems().add(cartItem);
        cart.getCartItems().add(CartItem.builder().item(Item.builder().price(300).build()).quantity(5).build());
        cart.getCartItems().remove(cartItem);
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        assertEquals(1500, cartService.totalPrice(1));
    }

    @Test
    public void testIfNoCartForGivenId() {
        when(cartRepository.findById(eq(1))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.totalPrice(1));
    }

    @Test
    public void testIfPurchaseWorksForTwoItems() {
        Cart cart = Cart.builder().id(1).build();
        cart.getCartItems().add(CartItem.builder().item(Item.builder().price(100).build()).quantity(5).build());
        cart.getCartItems().add(CartItem.builder().item(Item.builder().price(700).build()).quantity(1).build());
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        when(receiptService.createReceipt(eq(cart))).thenReturn(Receipt.builder().totalPrice(100 * 5 + 700).build());
        var receipt = cartService.purchase(1);
        assertEquals(100 * 5 + 700, receipt.getTotalPrice());
        verify(cartRepository).save(cartCaptor.capture());
        assertEquals(cart, cartCaptor.getValue());
    }

    @Test
    public void testIfPurchaseEmptyCart() {
        Cart cart = Cart.builder().id(1).build();
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        assertThrows(BadRequestException.class, () -> cartService.purchase(1));
    }

    @Test
    public void testIfNoCart() {
        when(cartRepository.findById(eq(1))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.totalPrice(1));
    }

    @Test
    public void testAddingWhenNoCartExists() {
        when(cartRepository.findById(eq(1))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.addItemToCart(1, CartItemDTO.builder().build()));
    }

    @Test
    public void testingAddingWhenNoItemExists() {
        when(itemService.findById(eq(2))).thenReturn(Optional.empty());
        var cart = Cart.builder().id(1).build();
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        assertThrows(NotFoundException.class, () -> cartService.addItemToCart(1, CartItemDTO.builder().itemId(2).build()));
    }

    @Test
    public void testAddingCartItemToEmptyCart() {
        var cart = Cart.builder().id(1).build();
        var item = Item.builder().id(1).name("Phone").price(7000).build();
        var cartItem = CartItem.builder().item(item).quantity(5).build();
        when(cartRepository.findById(eq(1))).thenReturn(Optional.of(cart));
        when(itemService.findById(eq(1))).thenReturn(Optional.of(item));
        cartService.addItemToCart(1, CartItemDTO.toDTO(cartItem));
        verify(cartRepository).save(cartCaptor.capture());
        cartItem.setCart(cart);
        assertEquals(List.of(cartItem), cartCaptor.getValue().getCartItems());
        assertEquals(cart, cartCaptor.getValue());
    }

    @Test
    public void testCreateCart() {
        var user = User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        when(cartRepository.save(any())).thenReturn(Cart.builder().user(user).build());
        var cart = cartService.createCart(user);
        verify(cartRepository).save(cartCaptor.capture());
        assertEquals(cart, cartCaptor.getValue());
    }

}
