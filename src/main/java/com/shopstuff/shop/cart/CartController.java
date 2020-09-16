package com.shopstuff.shop.cart;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;




@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("{id}/purchase")
    public int purchase(@PathVariable int id) {
        return cartService.purchase(id);
    }

    @GetMapping("{id}")
    public CartDTO showCart(@PathVariable int id) {
        return CartDTO.toDto(cartService.showCart(id));
    }

    @PostMapping("{id}/item")
    public CartDTO addItemToCart(@PathVariable int id,@RequestBody CartItemDTO cartItemDTO) {
        Cart cart=cartService.addItemToCart(id,cartItemDTO);
        return CartDTO.toDto(cart);
    }

}
