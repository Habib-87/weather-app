package com.example.weatherapp.service;

import com.example.weatherapp.client.GeocodingClient;
import com.example.weatherapp.client.WeatherClient;
import com.example.weatherapp.exception.CityNotFoundException;
import com.example.weatherapp.model.GeocodingResponse;
import com.example.weatherapp.model.GeocodingResponse.GeoLocation;
import com.example.weatherapp.model.OpenMeteoResponse;
import com.example.weatherapp.model.OpenMeteoResponse.CurrentWeather;
import com.example.weatherapp.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Orchestrates calls to the geocoding and weather clients,
 * then assembles the final {@link WeatherResponse}.
 */
@Slf4j
@Service
public class WeatherService {

    private final GeocodingClient geocodingClient;
    private final WeatherClient weatherClient;

    public WeatherService(GeocodingClient geocodingClient, WeatherClient weatherClient) {
        this.geocodingClient = geocodingClient;
        this.weatherClient = weatherClient;
    }

    public WeatherResponse getWeather(String cityName) {
        log.info("Fetching weather for city: {}", cityName);

        // Step 1 – resolve city to coordinates
        GeocodingResponse geocoding = geocodingClient.search(cityName);
        if (geocoding == null || CollectionUtils.isEmpty(geocoding.getResults())) {
            throw new CityNotFoundException(cityName);
        }
        GeoLocation location = geocoding.getResults().get(0);

        // Step 2 – fetch current weather
        OpenMeteoResponse meteo = weatherClient.getCurrentWeather(
                location.getLatitude(), location.getLongitude());

        CurrentWeather current = meteo.getCurrentWeather();

        return WeatherResponse.builder()
                .city(location.getName())
                .country(location.getCountry())
                .countryCode(location.getCountryCode())
                .region(location.getRegion())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .temperatureCelsius(current.getTemperature())
                .windspeedKmh(current.getWindspeed())
                .windDirectionDegrees(current.getWinddirection())
                .weatherCondition(resolveWeatherCondition(current.getWeatherCode()))
                .isDaytime(current.getIsDay() != null && current.getIsDay() == 1)
                .observationTime(current.getTime())
                .timezone(location.getTimezone())
                .build();
    }

    /**
     * Maps WMO Weather Interpretation Codes to human-readable descriptions.
     * Reference: https://open-meteo.com/en/docs#weathervariables
     */
    private String resolveWeatherCondition(Integer code) {
        if (code == null) {
            return "Unknown";
        }
        switch (code) {
            case 0:  return "Clear sky";
            case 1:  return "Mainly clear";
            case 2:  return "Partly cloudy";
            case 3:  return "Overcast";
            case 45: return "Fog";
            case 48: return "Depositing rime fog";
            case 51: return "Light drizzle";
            case 53: return "Moderate drizzle";
            case 55: return "Dense drizzle";
            case 56: return "Light freezing drizzle";
            case 57: return "Heavy freezing drizzle";
            case 61: return "Slight rain";
            case 63: return "Moderate rain";
            case 65: return "Heavy rain";
            case 66: return "Light freezing rain";
            case 67: return "Heavy freezing rain";
            case 71: return "Slight snow fall";
            case 73: return "Moderate snow fall";
            case 75: return "Heavy snow fall";
            case 77: return "Snow grains";
            case 80: return "Slight rain showers";
            case 81: return "Moderate rain showers";
            case 82: return "Violent rain showers";
            case 85: return "Slight snow showers";
            case 86: return "Heavy snow showers";
            case 95: return "Thunderstorm";
            case 96: return "Thunderstorm with slight hail";
            case 99: return "Thunderstorm with heavy hail";
            default: return "Unknown (code " + code + ")";
        }
    }
}
