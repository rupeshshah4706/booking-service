package com.example.booking.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DTO representing a booking request.
 * Includes logging and error handling in setters.
 */
public class BookingRequest {

    private static final Logger logger = LoggerFactory.getLogger(BookingRequest.class);

    private Long userId;
    private Long eventId;
    private String seatNumber;

    /**
     * Gets the user ID.
     * @return user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     * Logs at debug level and validates input.
     * @param userId user ID
     */
    public void setUserId(Long userId) {
        logger.debug("Setting userId: {}", userId);
        if (userId == null || userId <= 0) {
            logger.error("Invalid userId: {}", userId);
            throw new IllegalArgumentException("userId must be positive and not null");
        }
        this.userId = userId;
    }

    /**
     * Gets the event ID.
     * @return event ID
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID.
     * Logs at debug level and validates input.
     * @param eventId event ID
     */
    public void setEventId(Long eventId) {
        logger.debug("Setting eventId: {}", eventId);
        if (eventId == null || eventId <= 0) {
            logger.error("Invalid eventId: {}", eventId);
            throw new IllegalArgumentException("eventId must be positive and not null");
        }
        this.eventId = eventId;
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
            logger.warn("Attempted to set blank or null seatNumber");
            throw new IllegalArgumentException("seatNumber must not be blank");
        }
        this.seatNumber = seatNumber;
    }
}