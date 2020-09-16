package com.shopstuff.shop.cart;

import com.shopstuff.shop.item.Item;
import lombok.*;


import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @JoinColumn(name = "cartId")
    @ManyToOne
    @ToString.Exclude
    private Cart cart;

    @JoinColumn(name = "itemId")
    @ManyToOne
    private Item item;
    private int quantity;
}
