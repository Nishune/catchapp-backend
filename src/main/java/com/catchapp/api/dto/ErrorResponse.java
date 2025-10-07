package com.catchapp.api.dto;

public class ErrorResponse {
    private String error;

    public ErrorResponse() {}
    public ErrorResponse(String error) {
        this.error = error;
    }

    public static ErrorResponse of(String msg) {
        return new ErrorResponse(msg);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        // hELLO
    }
}
