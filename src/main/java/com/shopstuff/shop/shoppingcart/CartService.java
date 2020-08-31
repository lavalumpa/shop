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
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public void clearCart (int id){
        cartRepository.getOne(id).deleteAll();
    }


    @Transactional
    public int purchase(int id){
        var cart= cartRepository.findById(id).orElseThrow(NotFoundExceptions::new);
        int price = cart.getTotalPrice();
        cart.deleteAll();
        cartRepository.save(cart);
        return price;
    }

    public Cart createCart(User user){
        var cart= new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public Set<Item> showItems (int id){
        return cartRepository.findById(id).orElseThrow(NotFoundExceptions::new).getItems();
    }

    @Transactional
    public void addItemToCart(int cartId,int itemId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundExceptions::new);
        cart.addItem(itemRepository.findById(itemId).orElseThrow(NotFoundExceptions::new));
        cartRepository.save(cart);
    }


    public int totalPrice(int id){
        return cartRepository.findById(id).orElseThrow(NotFoundExceptions::new).getTotalPrice();
    }

}
