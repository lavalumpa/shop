package com.shopstuff.shop.delivery.weather;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "weather")
@Configuration
public class OpenWeatherProps {

    private String keyValue;
}
