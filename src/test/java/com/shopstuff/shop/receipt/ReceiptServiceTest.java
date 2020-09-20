package com.shopstuff.shop.receipt;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.cart.CartItem;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReceiptServiceTest {

    @InjectMocks
    private ReceiptService receiptService;

    @Captor
    private ArgumentCaptor<Receipt> captor;

    @Mock
    private ReceiptRepository receiptRepository;


    @Test
    public void testingIfNoReceiptsByUser(){
        var user= User.builder().id(1).name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        when(receiptRepository.findByUser(user)).thenReturn(new ArrayList<Receipt>());
        assertTrue(receiptService.receiptsByUser(user).isEmpty());
    }

    @Test
    public void testingIfTwoReceiptsByUser(){
        var user= User.builder().id(1).name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        var item= Item.builder().id(1).name("Phone").price(1000).build();
        var receipt1=Receipt.builder().user(user).createdAt(LocalDateTime.parse(LocalDateTime.now().toString())).totalPrice(1000).id(1).build();
        var receipt2=Receipt.builder().user(user).createdAt(LocalDateTime.parse(LocalDateTime.now().toString())).totalPrice(1000*2).id(1).build();
        var receiptItem1=ReceiptItem.builder().item(item).quantity(1).id(1).receipt(receipt1).build();
        var receiptItem2=ReceiptItem.builder().item(item).quantity(2).id(2).receipt(receipt1).build();
        receipt2.addReceiptItem(receiptItem2);
        receipt1.setTotalPrice(1000);
        receipt1.addReceiptItem(receiptItem1);
        when(receiptRepository.findByUser(user)).thenReturn(List.of(receipt1,receipt2));
        var receiptDTOList=receiptService.receiptsByUser(user);
        assertEquals(ReceiptDTO.toDTO(receipt1),receiptDTOList.get(0));
        assertEquals(ReceiptDTO.toDTO(receipt2),receiptDTOList.get(1));
    }

    @Test
    public void testingTotalPriceNoReceiptItems(){
        var receipt=Receipt.builder().build();
        assertEquals(0,receiptService.receiptTotalPrice(receipt));
    }

    @Test
    public void testingTotalPriceTwoReceiptItems(){
        var receipt=Receipt.builder().build();
        var receiptItem1=ReceiptItem.builder().item(Item.builder().price(500).build()).quantity(2).build();
        receipt.addReceiptItem(receiptItem1);
        var receiptItem2=ReceiptItem.builder().item(Item.builder().price(700).build()).quantity(3).build();
        receipt.addReceiptItem(receiptItem2);
        assertEquals(500*2+700*3,receiptService.receiptTotalPrice(receipt));
    }

    @Test
    public void testCreatingReceiptWithOneCartItemInCart(){
        var user=User.builder().id(1).build();
        var item=Item.builder().price(100).build();
        var cart= Cart.builder().id(1).user(user).build();
        cart.addCartItem(CartItem.builder().item(item).quantity(2).build());
        receiptService.createReceipt(cart);
        verify(receiptRepository).save(captor.capture());
        var receipt=captor.getValue();
        assertEquals(cart.getUser(),receipt.getUser());
        assertEquals(cart.getCartItems().get(0).getItem(),receipt.getReceiptItems().get(0).getItem());
    }
    @Test
    public void testCreatingReceiptWithNoCartItemInCart(){
        var user=User.builder().id(1).build();
        var item=Item.builder().price(100).build();
        var cart= Cart.builder().id(1).user(user).build();
        receiptService.createReceipt(cart);
        verify(receiptRepository).save(captor.capture());
        var receipt=captor.getValue();
        assertEquals(cart.getUser(),receipt.getUser());
        assertTrue(receipt.getReceiptItems().isEmpty());
    }


}
