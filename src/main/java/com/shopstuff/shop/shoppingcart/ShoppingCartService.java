package com.shopstuff.shop.shoppingcart;


import com.shopstuff.shop.exceptions.NotFoundExceptions;
import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.item.ItemRepository;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemRepository itemRepository;

    public void clearCart (int id){
        shoppingCartRepository.getOne(id).deleteAll();
    }


    @Transactional
    public int purchase(int id){
        var cart= shoppingCartRepository.findById(id).orElseThrow(NotFoundExceptions::new);
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

    @Transactional
    public void addItemToCart(int cartId,int itemId){
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElseThrow(NotFoundExceptions::new);
        cart.addItem(itemRepository.findById(itemId).orElseThrow(NotFoundExceptions::new));
        shoppingCartRepository.save(cart);
    }


    public int totalPrice(int id){
        return shoppingCartRepository.findById(id).orElseThrow(NotFoundExceptions::new).getTotalPrice();
    }

}
