package com.example.weatherapp.controller;

import com.example.weatherapp.model.WeatherResponse;
import com.example.weatherapp.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing weather endpoints.
 *
 * <ul>
 *   <li>GET /api/weather?city={cityName} – current weather for a given city</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Returns the current weather for the specified city.
     *
     * @param city the name of the city (e.g. London, Paris, Berlin)
     * @return {@link WeatherResponse} with temperature, wind, and conditions
     */
    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam(value = "city") String city) {
        WeatherResponse response = weatherService.getWeather(city.trim());
        return ResponseEntity.ok(response);
    }
}
