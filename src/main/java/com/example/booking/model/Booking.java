package com.example.booking.model;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Entity representing a booking for an event seat.
 * Includes logging for lifecycle events and error handling in setters.
 */
@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "seat_id"}))
public class Booking {

    private static final Logger logger = LoggerFactory.getLogger(Booking.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "status", length = 20)
    private String status = "BOOKED";

    @Column(name = "booked_at")
    private LocalDateTime bookedAt = LocalDateTime.now();

    /**
     * JPA lifecycle callback after loading the entity.
     */
    @PostLoad
    private void postLoad() {
        logger.debug("Loaded Booking entity with id: {}", id);
    }

    /**
     * JPA lifecycle callback before persisting the entity.
     */
    @PrePersist
    private void prePersist() {
        logger.info("Persisting new Booking: userId={}, eventId={}, seatId={}", userId, eventId, seatId);
        if (bookedAt == null) {
            bookedAt = LocalDateTime.now();
            logger.warn("bookedAt was null, set to now");
        }
    }

    // Getters and setters with error handling

    /**
     * Gets the booking ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the booking ID.
     * @param id the booking ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user ID.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * @param userId the user ID
     */
    public void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            logger.error("Invalid userId: {}", userId);
            throw new IllegalArgumentException("userId must be positive and not null");
        }
        this.userId = userId;
    }

    /**
     * Gets the event ID.
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID.
     * @param eventId the event ID
     */
    public void setEventId(Long eventId) {
        if (eventId == null || eventId <= 0) {
            logger.error("Invalid eventId: {}", eventId);
            throw new IllegalArgumentException("eventId must be positive and not null");
        }
        this.eventId = eventId;
    }

    /**
     * Gets the seat ID.
     */
    public Long getSeatId() {
        return seatId;
    }

    /**
     * Sets the seat ID.
     * @param seatId the seat ID
     */
    public void setSeatId(Long seatId) {
        if (seatId == null || seatId <= 0) {
            logger.error("Invalid seatId: {}", seatId);
            throw new IllegalArgumentException("seatId must be positive and not null");
        }
        this.seatId = seatId;
    }

    /**
     * Gets the booking status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the booking status.
     * @param status the booking status
     */
    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            logger.warn("Attempted to set blank or null status");
            throw new IllegalArgumentException("status must not be blank");
        }
        this.status = status;
    }

    /**
     * Gets the booking timestamp.
     */
    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    /**
     * Sets the booking timestamp.
     * @param bookedAt the booking timestamp
     */
    public void setBookedAt(LocalDateTime bookedAt) {
        if (bookedAt == null) {
            logger.warn("Attempted to set bookedAt to null, using current time");
            this.bookedAt = LocalDateTime.now();
        } else {
            this.bookedAt = bookedAt;
        }
    }
}