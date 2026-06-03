package com.example.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Maps the JSON response from the Open-Meteo Geocoding API.
 * Example: https://geocoding-api.open-meteo.com/v1/search?name=London&count=1
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResponse {

    private List<GeoLocation> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoLocation {

        private Long id;
        private String name;
        private Double latitude;
        private Double longitude;
        private Double elevation;

        @JsonProperty("country_code")
        private String countryCode;

        private String country;
        private String timezone;
        private Long population;

        @JsonProperty("admin1")
        private String region;
    }
}
