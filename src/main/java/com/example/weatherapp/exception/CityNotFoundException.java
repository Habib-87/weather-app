package com.example.weatherapp.exception;

/**
 * Thrown when a requested city cannot be found via the geocoding API.
 */
public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String city) {
        super("City not found: " + city);
    }
}
