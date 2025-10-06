package com.catchapp.api.dto;

import com.catchapp.model.User;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String createdAt;

    public UserDto() {}

    public static UserDto from(User u) {
        UserDto dto = new UserDto();
        dto.id = u.getId();
        dto.username = u.getUsername();
        dto.email = u.getEmail();
        dto.createdAt = u.getCreatedAt().toString();
        return dto;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getCreatedAt() { return createdAt; }
}
