package com.example.weatherapp.model;

import lombok.Builder;
import lombok.Data;

/**
 * Standard error response returned by the API when something goes wrong.
 */
@Data
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
}
