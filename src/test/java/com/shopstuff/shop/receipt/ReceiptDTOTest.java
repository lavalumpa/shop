package com.shopstuff.shop.receipt;

import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiptDTOTest {

    @Test
    public void testToDTOWhenReceiptOneReceiptItem() {
        var user = User.builder().id(1).build();
        var receipt = Receipt.builder().id(1).user(user)
                .createdAt(LocalDateTime.parse(LocalDateTime.now().toString()))
                .build();
        var item = Item.builder().id(1).price(50).build();
        receipt.addReceiptItem(ReceiptItem.builder().item(item).quantity(2).build());
        receipt.setTotalPrice(100);
        var receiptDTO = ReceiptDTO.toDTO(receipt);
        assertEquals(user.getId(), receiptDTO.getUserId());
        assertEquals(100, receiptDTO.getTotalPrice());
        assertEquals(1, receiptDTO.getId());
        assertEquals(1, receiptDTO.getReceiptItemDTOS().get(0).getItemId());
        assertEquals(2, receiptDTO.getReceiptItemDTOS().get(0).getQuantity());
        assertEquals(50, receiptDTO.getReceiptItemDTOS().get(0).getPrice());
    }

    @Test
    public void testToDTOWhenReceiptNoReceiptItem() {
        var user = User.builder().id(1).build();
        var receipt = Receipt.builder().id(1).user(user)
                .createdAt(LocalDateTime.parse(LocalDateTime.now().toString()))
                .build();
        var receiptDTO = ReceiptDTO.toDTO(receipt);
        assertEquals(user.getId(), receiptDTO.getUserId());
        assertEquals(0, receiptDTO.getTotalPrice());
        assertEquals(1, receiptDTO.getId());
        assertTrue(receipt.getReceiptItems().isEmpty());
    }
}
