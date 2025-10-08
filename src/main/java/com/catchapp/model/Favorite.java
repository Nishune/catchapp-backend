package com.catchapp.model;

import jakarta.persistence.*;

import java.time.Instant;


@Entity
@Table(name = "favorites",
        uniqueConstraints = @UniqueConstraint(name="ux_fav_user_activity", columnNames={"user_id","activity_id"}))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fav_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="activity_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_fav_activity"))
    private Activity activity;

    protected Favorite() {}

    private Favorite(Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt != null ? builder.createdAt : Instant.now();
        this.user = builder.user;
        this.activity = builder.activity;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
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
