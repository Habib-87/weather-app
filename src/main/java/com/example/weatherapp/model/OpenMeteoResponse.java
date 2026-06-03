package com.example.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Maps the JSON response from the Open-Meteo Weather Forecast API.
 * Example: https://api.open-meteo.com/v1/forecast?latitude=51.5&longitude=-0.12&current_weather=true
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoResponse {

    private Double latitude;
    private Double longitude;
    private Double elevation;
    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;

    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {

        private Double temperature;
        private Double windspeed;
        private Double winddirection;

        @JsonProperty("weathercode")
        private Integer weatherCode;

        @JsonProperty("is_day")
        private Integer isDay;

        private String time;
    }
}
