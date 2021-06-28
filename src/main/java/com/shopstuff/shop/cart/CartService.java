package com.shopstuff.shop.cart;


import com.shopstuff.shop.exceptions.BadRequestException;
import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.item.ItemService;
import com.shopstuff.shop.receipt.Receipt;
import com.shopstuff.shop.receipt.ReceiptService;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final ReceiptService receiptService;

    @Transactional
    public Receipt purchase(int id) {
        var cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException();
        }
        var receipt = receiptService.createReceipt(cart);
        cart.clear();
        cartRepository.save(cart);
        return receipt;
    }

    public Cart createCart(User user) {
        var cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCart(int id, CartDTO cartDTO) {
        var cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        cart.clear();
        update(cart, cartDTO);
        return cart;
    }

    private void update(Cart cart, CartDTO cartDTO) {
        for (CartItemDTO cartItemDTO : cartDTO.getCartItems()) {
            cart.addCartItem(createCartItem(cart, cartItemDTO));
        }
        cartRepository.save(cart);
    }

    private CartItem createCartItem(Cart cart, CartItemDTO cartDTO) {
        var item = itemService.findById(cartDTO.getItemId()).orElseThrow(NotFoundException::new);
        return CartItem.builder()
                .item(item)
                .cart(cart)
                .quantity(cartDTO.getQuantity())
                .build();
    }


    public Cart findById(int id) {
        return cartRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Cart addItemToCart(int cartId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        CartItem cartItem = CartItem.builder()
                .item(itemService.findById(cartItemDTO.getItemId()).orElseThrow(NotFoundException::new))
                .quantity(cartItemDTO.getQuantity())
                .build();
        cart.getCartItems()
                .stream()
                .filter(x -> x.getItem().getId() == cartItemDTO.getItemId())
                .findFirst()
                .ifPresentOrElse(x -> x.setQuantity(x.getQuantity() + cartItemDTO.getQuantity()), () -> cart.addCartItem(cartItem));
        cartRepository.save(cart);
        return cart;
    }


    public int totalPrice(int id) {
        return cartRepository.findById(id).orElseThrow(NotFoundException::new).getCartItems()
                .stream().map((x) -> x.getQuantity() * x.getItem().getPrice()).reduce(Integer::sum).orElse(0);
    }

    public boolean correctUser(String userName, int id) {
        return cartRepository.findById(id)
                .map(Cart::getUser)
                .map(User::getName)
                .filter(x -> x.equals(userName))
                .isPresent();
    }


}
