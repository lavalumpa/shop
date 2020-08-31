package com.shopstuff.shop.shoppingcart;


import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany
    @JoinTable (name = "cart_item",
            joinColumns = {@JoinColumn ( name = "cartID")},
            inverseJoinColumns = {@JoinColumn(name = "itemID") })
    private Set<Item> items = new HashSet<>();

    public void addItem(Item item){
        items.add(item);
    }

    public void deleteItem (Item item){
        items.remove(item);
    }



    public int getTotalPrice(){
        return items.stream().map(Item::getPrice).reduce(Integer::sum).orElseThrow();
    }

    public void deleteAll(){
        items.clear();
    }

}
