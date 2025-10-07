package com.catchapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Username required")
    @Size(min = 3, max = 50, message= "Username must be between 3 and 50 characters")
    private String username;
    @NotBlank(message = "Password required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 and 100 characters")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
