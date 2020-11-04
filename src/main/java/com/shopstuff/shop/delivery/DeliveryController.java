package com.shopstuff.shop.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_MANAGER') or (hasRole('CUSTOMER') and @deliveryService.correctUser(principal.username,#id))")
    @GetMapping("{id}")
    public DeliveryDTO getDeliveryStatus(@PathVariable int id) {
        var delivery = deliveryService.findById(id);
        return DeliveryDTO.toDTO(delivery);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DELIVERY_MANAGER')")
    @PatchMapping("{id}")
    public DeliveryDTO updateDeliveryStatus(@PathVariable int id, @RequestBody DeliveryDTO deliveryDTO) {
        var delivery = deliveryService.findById(id);
        deliveryService.updateDeliveryState(deliveryDTO, delivery);
        deliveryService.saveOrUpdate(delivery);
        return DeliveryDTO.toDTO(delivery);
    }


}



