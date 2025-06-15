// src/main/java/com/example/booking/dto/EventWithSeats.java
package com.example.booking.model;

import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

/**
 * DTO representing an event along with its paginated seats.
 * Includes logging and error handling in constructor and setters.
 */
public class EventWithSeats {

    private static final Logger logger = LoggerFactory.getLogger(EventWithSeats.class);

    private Event event;
    private Page<Seat> seats;

    /**
     * Constructs an EventWithSeats object.
     * Logs at info level and validates input.
     * @param event the event
     * @param seats the paginated seats
     */
    public EventWithSeats(Event event, Page<Seat> seats) {
        logger.info("Creating EventWithSeats for event: {}", event != null ? event.getId() : null);
        if (event == null) {
            logger.error("Event must not be null");
            throw new IllegalArgumentException("Event must not be null");
        }
        if (seats == null) {
            logger.error("Seats page must not be null");
            throw new IllegalArgumentException("Seats page must not be null");
        }
        this.event = event;
        this.seats = seats;
    }

    /**
     * Gets the event.
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event.
     * Logs at debug level and validates input.
     * @param event the event
     */
    public void setEvent(Event event) {
        logger.debug("Setting event: {}", event != null ? event.getId() : null);
        if (event == null) {
            logger.error("Event must not be null");
            throw new IllegalArgumentException("Event must not be null");
        }
        this.event = event;
    }

    /**
     * Gets the paginated seats.
     * @return the seats
     */
    public Page<Seat> getSeats() {
        return seats;
    }

    /**
     * Sets the paginated seats.
     * Logs at debug level and validates input.
     * @param seats the paginated seats
     */
    public void setSeats(Page<Seat> seats) {
        logger.debug("Setting seats page");
        if (seats == null) {
            logger.error("Seats page must not be null");
            throw new IllegalArgumentException("Seats page must not be null");
        }
        this.seats = seats;
    }
}