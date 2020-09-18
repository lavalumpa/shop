package com.shopstuff.shop.cart;


import com.shopstuff.shop.user.User;
import lombok.*;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "cart",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }
    public void clear(){
        cartItems.clear();
    }


}
