package com.shopstuff.shop.delivery;

import com.shopstuff.shop.cart.Cart;
import com.shopstuff.shop.delivery.weather.OpenWeatherAPI;
import com.shopstuff.shop.delivery.weather.WeatherDTO;
import com.shopstuff.shop.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    @InjectMocks
    private DeliveryService deliveryService;
    @Mock
    private OpenWeatherAPI openWeatherAPI;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Captor
    private ArgumentCaptor<Delivery> deliveryCaptor;


    @Test
    public void testingEstimatedDaysForBelgradeWithRain() {
        var address = Address.builder().city("Belgrade").street("Gandijeva").number(58).build();
        when(openWeatherAPI.cityDayForecast(eq(address.getCity()),anyInt(),any())).thenReturn(createWeatherDTO("Rain"));
        assertThat(deliveryService.estimatedDays(createDeliveryDTO(address,DeliveryState.NOT_PROCESSED))).isEqualTo(2);
    }


    @Test
    public void testingEstimatedDaysForPetrovacWithRainInBelgradeNoRainInPetrovac() {
        var address = Address.builder().city("Petrovac").street("Srpskih vladara").number(58).build();
        when(openWeatherAPI.cityDayForecast(eq("Belgrade"),anyInt(),any()))
                .thenReturn(createWeatherDTO("Rain"));
        when(openWeatherAPI.cityDayForecast(eq(address.getCity()),anyInt(),any())).thenReturn(createWeatherDTO("Clear"));
        assertThat(deliveryService.estimatedDays(createDeliveryDTO(address,DeliveryState.NOT_PROCESSED))).isEqualTo(4);
    }

    @Test
    public void testCreateDeliveryForBelgrade() {
        var address = Address.builder().city("Belgrade").street("Gandijeva").number(58).build();
        var deliveryDTO = createDeliveryDTO(address,DeliveryState.NOT_PROCESSED);
        var cart = Cart.builder().user(User.builder().id(1).name("name").build()).build();
        when(openWeatherAPI.cityDayForecast(eq(address.getCity()),anyInt(),any())).thenReturn(createWeatherDTO("Rain"));
        deliveryService.createDelivery(cart, deliveryDTO);
        verify(deliveryRepository).save(deliveryCaptor.capture());
        assertThat(deliveryCaptor.getValue().getEstimatedDate()).isEqualTo(LocalDate.now().plusDays(2));
    }

    @Test
    public void testStateChangeToDelivered(){
        var address=Address.builder().build();
        var deliveryDTO=createDeliveryDTO(address,DeliveryState.DELIVERED);
        var delivery=Delivery.builder().deliveryState(DeliveryState.IN_TRANSIT).build();
        deliveryService.updateDeliveryState(deliveryDTO,delivery);
        assertThat(delivery.getDeliveryState()).isEqualTo(deliveryDTO.getDeliveryState());
        assertThat(delivery.getDeliveredOn().toLocalDate()).isAfterOrEqualTo(LocalDate.now());
    }

    @Test
    public void testStateChangeToInTransit(){
        var address=Address.builder().build();
        var deliveryDTO=createDeliveryDTO(address,DeliveryState.IN_TRANSIT);
        var delivery=Delivery.builder().deliveryState(DeliveryState.NOT_PROCESSED).build();
        deliveryService.updateDeliveryState(deliveryDTO,delivery);
        assertThat(delivery.getDeliveryState()).isEqualTo(deliveryDTO.getDeliveryState());
        assertThat(delivery.getDeliveredOn()).isNull();
    }


    public static WeatherDTO createWeatherDTO(String main) {
        var weatherDTO = new WeatherDTO();
        var weather = new WeatherDTO.WeatherInfoDTO.Weather();
        weather.setMain(main);
        var weatherInfo = new WeatherDTO.WeatherInfoDTO();
        weatherInfo.setWeather(List.of(weather));
        weatherInfo.setForecastTime(LocalDateTime.now());
        weatherDTO.setWeatherInfoList(List.of(weatherInfo));
        return weatherDTO;
    }


    public static DeliveryDTO createDeliveryDTO(Address address, DeliveryState deliveryState) {
        return DeliveryDTO.builder().address(address).createdAt(LocalDateTime.now()).deliveryState(deliveryState)
                .address(address).build();
    }
}
