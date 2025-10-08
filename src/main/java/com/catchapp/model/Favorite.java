package com.catchapp.model;

import java.time.Instant;

public class Favorite {
    private Long id;
    private Instant createdAt;
    private User user;
    private Activity activity;

    public Favorite() {}

    private Favorite(Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.user = builder.user;
        this.activity = builder.activity;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Instant createdAt;
        private User user;
        private Activity activity;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder activity(Activity activity) { this.activity = activity; return this; }

        public Favorite build() { return new Favorite(this); }
    }

    public Long getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public User getUser() { return user; }
    public Activity getActivity() { return activity; }
}
