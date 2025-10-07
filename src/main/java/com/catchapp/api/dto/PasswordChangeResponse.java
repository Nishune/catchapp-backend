package com.catchapp.api.dto;

public class PasswordChangeResponse {
    private String message;

    public PasswordChangeResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
