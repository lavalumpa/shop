package com.shopstuff.shop.delivery;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;


    public Delivery createDelivery(Cart cart, DeliveryDTO deliveryDTO) {
        var delivery = Delivery.builder()
                .user(cart.getUser())
                .city(deliveryDTO.getAddress().getCity())
                .street(deliveryDTO.getAddress().getStreet())
                .number(deliveryDTO.getAddress().getNumber())
                .deliveryState(DeliveryState.NOT_PROCESSED)
                .build();
        int days = 1;
        if (!deliveryDTO.getAddress().getCity().equalsIgnoreCase("belgrade")) {
            days += 2;
        }
        var estimatedDate = LocalDate.now().plusDays(days);
        delivery.setEstimatedDate(estimatedDate);
        return deliveryRepository.save(delivery);
    }

    public Delivery findById(int id) {
        return deliveryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }


    public void updateDeliveryState(DeliveryDTO deliveryDTO, Delivery delivery) {
        delivery.setDeliveryState(deliveryDTO.getDeliveryState());
    }

    public Delivery saveOrUpdate(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public boolean correctUser(String username, int id) {
        return deliveryRepository.findById(id)
                .map(Delivery::getUser)
                .map(User::getName)
                .filter(x -> x.equals(username))
                .isPresent();
    }

}
