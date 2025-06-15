package com.example.booking.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * DTO representing a request to create or update an event.
 * Includes logging and error handling in setters.
 */
public class EventRequest {

    private static final Logger logger = LoggerFactory.getLogger(EventRequest.class);

    private String name;
    private String location;
    private LocalDateTime eventDate;
    private Integer totalSeats;
    private Integer seatStart; // e.g., 1
    private Integer seatEnd;   // e.g., 100

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
     * Gets the starting seat number.
     * @return seat start
     */
    public Integer getSeatStart() {
        return seatStart;
    }

    /**
     * Sets the starting seat number.
     * Logs at debug level and validates input.
     * @param seatStart seat start
     */
    public void setSeatStart(Integer seatStart) {
        logger.debug("Setting seatStart: {}", seatStart);
        if (seatStart == null || seatStart <= 0) {
            logger.error("Invalid seatStart: {}", seatStart);
            throw new IllegalArgumentException("Seat start must be positive and not null");
        }
        this.seatStart = seatStart;
    }

    /**
     * Gets the ending seat number.
     * @return seat end
     */
    public Integer getSeatEnd() {
        return seatEnd;
    }

    /**
     * Sets the ending seat number.
     * Logs at debug level and validates input.
     * @param seatEnd seat end
     */
    public void setSeatEnd(Integer seatEnd) {
        logger.debug("Setting seatEnd: {}", seatEnd);
        if (seatEnd == null || seatEnd < seatStart) {
            logger.error("Invalid seatEnd: {} (seatStart: {})", seatEnd, seatStart);
            throw new IllegalArgumentException("Seat end must be greater than or equal to seat start and not null");
        }
        this.seatEnd = seatEnd;
    }
}