package com.shopstuff.shop.shoppingcart;


import com.shopstuff.shop.exceptions.NotFoundExceptions;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;


    public void clearCart (int id){
        shoppingCartRepository.getOne(id).deleteAll();
    }


    public int purchase(int id){
        ShoppingCart cart= shoppingCartRepository.findById(id).orElseThrow(NotFoundExceptions::new);
        int price = cart.getTotalPrice();
        cart.deleteAll();
        shoppingCartRepository.save(cart);
        return price;
    }

    public ShoppingCart createShoppingCart(User user){
        var cart= new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }

    public Set<Item> showItems (int id){
        return shoppingCartRepository.findById(id).orElseThrow(NotFoundExceptions::new).getItems();
    }

    public void addItemToCart(Item item, ShoppingCart shoppingCart){
        shoppingCart.addItem(item);
        shoppingCartRepository.save(shoppingCart);
    }


    public int totalPrice(int id){
        return shoppingCartRepository.findById(id).orElseThrow(NotFoundExceptions::new).getTotalPrice();
    }

}
