package com.shopstuff.shop.cart;


import com.shopstuff.shop.delivery.DeliveryDTO;
import com.shopstuff.shop.delivery.DeliveryService;
import com.shopstuff.shop.receipt.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;




@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;
    private final DeliveryService deliveryService;


    @PostMapping("{id}/purchase")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public ResponseEntity<ReceiptDTO> purchase(@PathVariable int id, @RequestBody DeliveryDTO deliveryDTO) {
        var cart=cartService.findById(id);
        var receipt=cartService.purchase(id);
        if (deliveryDTO.isDeliveryRequested()){
            if (deliveryDTO.infoProvided()){
                deliveryService.createDelivery(cart, deliveryDTO);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(ReceiptDTO.toDTO(receipt));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public CartDTO showCart(@PathVariable int id) {
        return CartDTO.toDTO(cartService.findById(id));
    }

    @PostMapping("{id}/item")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and @cartService.correctUser(principal.username,#id))")
    public CartDTO addItemToCart(@PathVariable int id,@RequestBody CartItemDTO cartItemDTO) {
        Cart cart=cartService.addItemToCart(id,cartItemDTO);
        return CartDTO.toDTO(cart);
    }

}
