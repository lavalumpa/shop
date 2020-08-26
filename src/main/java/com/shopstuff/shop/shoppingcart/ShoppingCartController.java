package com.shopstuff.shop.shoppingcart;


import com.shopstuff.shop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("{id}/purchase")
    public int purchase(@PathVariable int id){
        return shoppingCartService.purchase(id);
    }

    @GetMapping("{id}/items")
    public Set<Item> showCartItems(@PathVariable int id){
        return shoppingCartService.showItems(id);
    }



}
