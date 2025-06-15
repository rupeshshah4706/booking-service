// src/main/java/com/example/booking/model/Seat.java
package com.example.booking.model;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity representing a seat for an event.
 * Includes logging for lifecycle events and error handling in setters.
 */
@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "seat_number"}))
public class Seat {

    private static final Logger logger = LoggerFactory.getLogger(Seat.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "is_booked")
    private Boolean isBooked = false;

    /**
     * JPA lifecycle callback after loading the entity.
     */
    @PostLoad
    private void postLoad() {
        logger.debug("Loaded Seat entity with id: {}", id);
    }

    /**
     * JPA lifecycle callback before persisting the entity.
     */
    @PrePersist
    private void prePersist() {
        logger.info("Persisting new Seat: seatNumber={}, eventId={}", seatNumber, event != null ? event.getId() : null);
        if (isBooked == null) {
            isBooked = false;
            logger.warn("isBooked was null, set to false");
        }
    }

    /**
     * Gets the seat ID.
     * @return seat ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the seat ID.
     * @param id seat ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the event associated with the seat.
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event associated with the seat.
     * Logs at debug level and validates input.
     * @param event event
     */
    public void setEvent(Event event) {
        logger.debug("Setting event for seat id={}", id);
        if (event == null) {
            logger.error("Event must not be null");
            throw new IllegalArgumentException("Event must not be null");
        }
        this.event = event;
    }

    /**
     * Gets the seat number.
     * @return seat number
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number.
     * Logs at info level and validates input.
     * @param seatNumber seat number
     */
    public void setSeatNumber(String seatNumber) {
        logger.info("Setting seatNumber: {}", seatNumber);
        if (seatNumber == null || seatNumber.isBlank()) {
            logger.error("Invalid seatNumber: {}", seatNumber);
            throw new IllegalArgumentException("Seat number must not be blank");
        }
        this.seatNumber = seatNumber;
    }

    /**
     * Gets the booking status of the seat.
     * @return true if booked, false otherwise
     */
    public Boolean getIsBooked() {
        return isBooked;
    }

    /**
     * Sets the booking status of the seat.
     * Logs at debug level and validates input.
     * @param isBooked booking status
     */
    public void setIsBooked(Boolean isBooked) {
        logger.debug("Setting isBooked: {}", isBooked);
        if (isBooked == null) {
            logger.warn("Attempted to set isBooked to null, using false");
            this.isBooked = false;
        } else {
            this.isBooked = isBooked;
        }
    }
}