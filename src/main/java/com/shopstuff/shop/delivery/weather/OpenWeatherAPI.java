package com.shopstuff.shop.delivery.weather;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "weahterAPI",url = "http://api.openweathermap.org/data/2.5/forecast")
public interface OpenWeatherAPI {

    @GetMapping
    WeatherDTO cityDayForecast(@RequestParam(name = "q") String city,
                               @RequestParam(name = "cnt")int interval,
                               @RequestParam(name = "appid") String weatherMapAPIKey );
}
