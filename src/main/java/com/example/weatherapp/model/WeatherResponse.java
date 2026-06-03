package com.example.weatherapp.model;

import lombok.Builder;
import lombok.Data;

/**
 * The response payload returned by the Weather REST API to clients.
 */
@Data
@Builder
public class WeatherResponse {

    private String city;
    private String country;
    private String countryCode;
    private String region;
    private Double latitude;
    private Double longitude;
    private Double temperatureCelsius;
    private Double windspeedKmh;
    private Double windDirectionDegrees;
    private String weatherCondition;
    private Boolean isDaytime;
    private String observationTime;
    private String timezone;
}
