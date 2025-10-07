package com.catchapp.model;

import java.time.LocalDate;

public class Activity {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private String imageUrl;

    public Activity() {}

    private Activity(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.date = builder.date;
        this.location = builder.location;
        this.imageUrl = builder.imageUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private LocalDate date;
        private String location;
        private String imageUrl;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

        public Activity build() {
            return new Activity(this);
        }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }

    public void setId(Long id) { this.id = id; }
}
