package com.shopstuff.shop.receipt;

import com.shopstuff.shop.cart.CartItem;
import com.shopstuff.shop.item.Item;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Builder
public class ReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "receiptId")
    @ManyToOne
    @ToString.Exclude
    private Receipt receipt;

    @JoinColumn(name = "itemId")
    @ManyToOne
    private Item item;
    private int quantity;

    public static ReceiptItem toReceiptItem(CartItem cartItem){
        return ReceiptItem.builder()
                .item(cartItem.getItem())
                .quantity(cartItem.getQuantity()).build();
    }

}
