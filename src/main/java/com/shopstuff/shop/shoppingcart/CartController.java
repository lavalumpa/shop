package com.shopstuff.shop.shoppingcart;


import com.shopstuff.shop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("{id}/purchase")
    public int purchase(@PathVariable int id) {
        return cartService.purchase(id);
    }

    @GetMapping("{id}/items")
    public Set<Item> showCartItems(@PathVariable int id) {
        return cartService.showItems(id);
    }

    @PutMapping("{cartId}/item/{itemId}")
    public void addItemToCart(@PathVariable int cartid, @PathVariable int itemId) {
        cartService.addItemToCart(cartid,itemId);
    }

}
