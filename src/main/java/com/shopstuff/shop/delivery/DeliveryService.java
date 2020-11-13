package com.shopstuff.shop.delivery;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.delivery.weather.WeatherDTO;
import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;

    @Value("${OPEN_WEATHER_MAP_KEY}")
    private String weatherMapAPIKey;


    public Delivery createDelivery(Cart cart, DeliveryDTO deliveryDTO) {
        var delivery = Delivery.builder()
                .user(cart.getUser())
                .city(deliveryDTO.getAddress().getCity())
                .street(deliveryDTO.getAddress().getStreet())
                .number(deliveryDTO.getAddress().getNumber())
                .deliveryState(DeliveryState.NOT_PROCESSED)
                .build();
        delivery.setEstimatedDate(LocalDate.now().plusDays(estimatedDays(deliveryDTO)));
        return deliveryRepository.save(delivery);
    }

    public int estimatedDays(DeliveryDTO deliveryDTO) {
        int days = 1;
        var weatherBelgrade = cityDayForecast("Belgrade", 8, restTemplate);
        if (weatherBelgrade.hasRainOrSnow()) {
            days += 1;
        }
        if (!deliveryDTO.getAddress().getCity().equalsIgnoreCase("belgrade")) {
            days += 2;
            var weatherDeliveryCity = cityDayForecast(deliveryDTO.getAddress().getCity(), 24, restTemplate);
            if (weatherDeliveryCity.hasRainOrSnow()) {
                days += 1;
            }
        }
        return days;
    }

    public WeatherDTO cityDayForecast(String city, int interval, RestTemplate restTemplate) {
        var uri = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/data/2.5/forecast")
                .queryParam("q", city)
                .queryParam("cnt", interval)
                .queryParam("appid", weatherMapAPIKey).toUriString();
        return restTemplate.getForObject(uri, WeatherDTO.class);
    }

    public Delivery findById(int id) {
        return deliveryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }


    public void updateDeliveryState(DeliveryDTO deliveryDTO, Delivery delivery) {
        var state = deliveryDTO.getDeliveryState();
        delivery.setDeliveryState(state);
        if (state == DeliveryState.DELIVERED) {
            delivery.setDeliveredOn(LocalDateTime.now());
        }
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
