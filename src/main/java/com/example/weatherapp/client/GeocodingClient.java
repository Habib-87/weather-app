package com.example.weatherapp.client;

import com.example.weatherapp.exception.WeatherServiceException;
import com.example.weatherapp.model.GeocodingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * HTTP client for the Open-Meteo Geocoding API.
 * Converts a city name into geographic coordinates (latitude / longitude).
 */
@Slf4j
@Component
public class GeocodingClient {

    private final RestTemplate restTemplate;
    private final String geocodingUrl;

    public GeocodingClient(RestTemplate restTemplate,
                           @Value("${openmeteo.geocoding.url}") String geocodingUrl) {
        this.restTemplate = restTemplate;
        this.geocodingUrl = geocodingUrl;
    }

    public GeocodingResponse search(String cityName) {
        URI uri = UriComponentsBuilder.fromHttpUrl(geocodingUrl)
                .queryParam("name", cityName)
                .queryParam("count", 1)
                .queryParam("language", "en")
                .queryParam("format", "json")
                .build()
                .toUri();

        log.debug("Calling geocoding API: {}", uri);

        try {
            GeocodingResponse response = restTemplate.getForObject(uri, GeocodingResponse.class);
            return response;
        } catch (RestClientException ex) {
            throw new WeatherServiceException(
                    "Failed to contact the geocoding service for city: " + cityName, ex);
        }
    }
}
