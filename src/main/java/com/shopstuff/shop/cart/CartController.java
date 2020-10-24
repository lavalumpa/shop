package com.shopstuff.shop.cart;


import com.shopstuff.shop.receipt.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;




@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;



    @PostMapping("{id}/purchase")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public ReceiptDTO purchase(@PathVariable int id) {
        var receipt=cartService.purchase(id);
        return ReceiptDTO.toDTO(receipt);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public CartDTO showCart(@PathVariable int id) {
        return CartDTO.toDTO(cartService.showCart(id));
    }

    @PostMapping("{id}/item")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public CartDTO addItemToCart(@PathVariable int id,@RequestBody CartItemDTO cartItemDTO) {
        Cart cart=cartService.addItemToCart(id,cartItemDTO);
        return CartDTO.toDTO(cart);
    }

}
