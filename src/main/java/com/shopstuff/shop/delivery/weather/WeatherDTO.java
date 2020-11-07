package com.shopstuff.shop.delivery.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WeatherDTO {
    @JsonProperty("list")
    private List<WeatherInfoDTO> weatherInfoList;

    @Data
    public static class WeatherInfoDTO {

        private List<Weather> weather;
        @JsonProperty("dt_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD-MM-YYYY  hh:mm:ss")
        private LocalDateTime forecastTime;

        @Data
        public static class Weather {
            private String main;
        }

        public boolean isRainingOrSnowing() {
            if (this.weather.isEmpty() || this.weather.get(0).main.isEmpty()) {
                return false;
            }
            var weather = this.weather.get(0).getMain();
            return weather.equalsIgnoreCase("rain") || weather.equalsIgnoreCase("snow");
        }

    }


    public boolean hasRainOrSnow() {
        return this.weatherInfoList.stream()
                .anyMatch(WeatherInfoDTO::isRainingOrSnowing);
    }
}
