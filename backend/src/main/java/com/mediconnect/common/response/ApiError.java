package com.mediconnect.common.response;

public class ApiError {
    private final String message;
    private final String details;

    public ApiError(String message, String details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
