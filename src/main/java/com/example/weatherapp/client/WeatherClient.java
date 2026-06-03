package com.example.weatherapp.client;

import com.example.weatherapp.exception.WeatherServiceException;
import com.example.weatherapp.model.OpenMeteoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * HTTP client for the Open-Meteo Weather Forecast API.
 * Fetches the current weather for a given latitude/longitude.
 */
@Slf4j
@Component
public class WeatherClient {

    private final RestTemplate restTemplate;
    private final String weatherUrl;

    public WeatherClient(RestTemplate restTemplate,
                         @Value("${openmeteo.weather.url}") String weatherUrl) {
        this.restTemplate = restTemplate;
        this.weatherUrl = weatherUrl;
    }

    public OpenMeteoResponse getCurrentWeather(double latitude, double longitude) {
        URI uri = UriComponentsBuilder.fromHttpUrl(weatherUrl)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current_weather", true)
                .queryParam("wind_speed_unit", "kmh")
                .build()
                .toUri();

        log.debug("Calling weather API: {}", uri);

        try {
            OpenMeteoResponse response = restTemplate.getForObject(uri, OpenMeteoResponse.class);
            return response;
        } catch (RestClientException ex) {
            throw new WeatherServiceException(
                    "Failed to fetch weather data for coordinates: " + latitude + ", " + longitude, ex);
        }
    }
}
