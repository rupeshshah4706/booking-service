package com.example.booking.model;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Entity representing a user in the booking system.
 * Includes logging for lifecycle events and error handling in setters.
 */
@Entity
@Table(name = "users")
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * JPA lifecycle callback before persisting the entity.
     * Sets the createdAt timestamp.
     */
    @PrePersist
    protected void onCreate() {
        logger.info("Persisting new User: name={}, email={}", name, email);
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the user ID.
     * @return user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID.
     * @param id user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user's name.
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     * Logs at info level and validates input.
     * @param name user name
     */
    public void setName(String name) {
        logger.info("Setting user name: {}", name);
        if (name == null || name.isBlank()) {
            logger.error("Invalid user name: {}", name);
            throw new IllegalArgumentException("User name must not be blank");
        }
        this.name = name;
    }

    /**
     * Gets the user's email.
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     * Logs at info level and validates input.
     * @param email user email
     */
    public void setEmail(String email) {
        logger.info("Setting user email: {}", email);
        if (email == null || email.isBlank() || !email.contains("@")) {
            logger.error("Invalid user email: {}", email);
            throw new IllegalArgumentException("User email must be valid and not blank");
        }
        this.email = email;
    }

    /**
     * Gets the creation timestamp.
     * @return created at
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * Logs at debug level and validates input.
     * @param createdAt created at
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        logger.debug("Setting createdAt: {}", createdAt);
        if (createdAt == null) {
            logger.warn("Attempted to set createdAt to null, using current time");
            this.createdAt = LocalDateTime.now();
        } else {
            this.createdAt = createdAt;
        }
    }
}