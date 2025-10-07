package com.catchapp.api.dto;

public class LogoutReponse {

    private String message;

    public LogoutReponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
