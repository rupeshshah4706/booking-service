// src/main/java/com/example/booking/model/Event.java
package com.example.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity representing an event.
 * Includes logging for lifecycle events and error handling in setters.
 */
@Entity
@Table(name = "events")
public class Event {

    private static final Logger logger = LoggerFactory.getLogger(Event.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Seat> seats;

    /**
     * JPA lifecycle callback before persisting the entity.
     * Sets the createdAt timestamp.
     */
    @PrePersist
    protected void onCreate() {
        logger.info("Persisting new Event: name={}, location={}", name, location);
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the list of seats for the event.
     * @return list of seats
     */
    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * Sets the list of seats for the event.
     * @param seats list of seats
     */
    public void setSeats(List<Seat> seats) {
        logger.debug("Setting seats for event id={}", id);
        this.seats = seats;
    }

    /**
     * Gets the event ID.
     * @return event ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the event ID.
     * @param id event ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the event name.
     * @return event name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the event name.
     * Logs at info level and validates input.
     * @param name event name
     */
    public void setName(String name) {
        logger.info("Setting event name: {}", name);
        if (name == null || name.isBlank()) {
            logger.error("Invalid event name: {}", name);
            throw new IllegalArgumentException("Event name must not be blank");
        }
        this.name = name;
    }

    /**
     * Gets the event location.
     * @return event location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the event location.
     * Logs at info level and validates input.
     * @param location event location
     */
    public void setLocation(String location) {
        logger.info("Setting event location: {}", location);
        if (location == null || location.isBlank()) {
            logger.warn("Attempted to set blank or null location");
            throw new IllegalArgumentException("Event location must not be blank");
        }
        this.location = location;
    }

    /**
     * Gets the event date.
     * @return event date
     */
    public LocalDateTime getEventDate() {
        return eventDate;
    }

    /**
     * Sets the event date.
     * Logs at debug level and validates input.
     * @param eventDate event date
     */
    public void setEventDate(LocalDateTime eventDate) {
        logger.debug("Setting event date: {}", eventDate);
        if (eventDate == null || eventDate.isBefore(LocalDateTime.now())) {
            logger.error("Invalid event date: {}", eventDate);
            throw new IllegalArgumentException("Event date must be in the future and not null");
        }
        this.eventDate = eventDate;
    }

    /**
     * Gets the total number of seats.
     * @return total seats
     */
    public Integer getTotalSeats() {
        return totalSeats;
    }

    /**
     * Sets the total number of seats.
     * Logs at debug level and validates input.
     * @param totalSeats total seats
     */
    public void setTotalSeats(Integer totalSeats) {
        logger.debug("Setting totalSeats: {}", totalSeats);
        if (totalSeats == null || totalSeats <= 0) {
            logger.error("Invalid totalSeats: {}", totalSeats);
            throw new IllegalArgumentException("Total seats must be positive and not null");
        }
        this.totalSeats = totalSeats;
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