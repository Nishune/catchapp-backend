package com.catchapp.api.dto;

public class LogoutResponse {

    private String message;

    public LogoutResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
