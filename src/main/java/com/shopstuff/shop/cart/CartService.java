package com.shopstuff.shop.cart;


import com.shopstuff.shop.exceptions.NotFoundException;



import com.shopstuff.shop.item.ItemService;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemService itemService;

    @Transactional
    public int purchase(int id) {
        var cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        int price = totalPrice(id);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return price;
    }

    public Cart createCart(User user) {
        var cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }


    public Cart showCart(int id) {
        return cartRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Cart addItemToCart(int cartId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        final CartItem cartItem = CartItem.builder()
                .item(itemService.findById(cartItemDTO.getItemId()).orElseThrow(NotFoundException::new))
                .quantity(cartItemDTO.getQuantity())
                .build();
        cart.addCartItem(cartItem);
        cartRepository.save(cart);
        return cart;
    }


    public int totalPrice(int id) {
        return cartRepository.findById(id).orElseThrow(NotFoundException::new).getCartItems()
                .stream().map((x)->x.getQuantity()*x.getItem().getPrice()).reduce(Integer::sum).orElse(0);
    }

}
