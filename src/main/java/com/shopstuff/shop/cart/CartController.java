package com.shopstuff.shop.cart;


import com.shopstuff.shop.receipt.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;




@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;


    @PostMapping("{id}/purchase")
    public ReceiptDTO purchase(@PathVariable int id) {
        var receipt=cartService.purchase(id);
        return ReceiptDTO.toDTO(receipt);
    }

    @GetMapping("{id}")
    public CartDTO showCart(@PathVariable int id) {
        return CartDTO.toDTO(cartService.showCart(id));
    }

    @PostMapping("{id}/item")
    public CartDTO addItemToCart(@PathVariable int id,@RequestBody CartItemDTO cartItemDTO) {
        Cart cart=cartService.addItemToCart(id,cartItemDTO);
        return CartDTO.toDTO(cart);
    }

}
