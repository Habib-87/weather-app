package com.example.weatherapp.exception;

/**
 * Thrown when the upstream Open-Meteo API call fails.
 */
public class WeatherServiceException extends RuntimeException {

    public WeatherServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
