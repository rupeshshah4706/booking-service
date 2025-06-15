package com.example.booking.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DTO representing a request to update the booking status of a seat.
 * Includes logging and error handling in constructor and setters.
 */
public class SeatStatusUpdate {

    private static final Logger logger = LoggerFactory.getLogger(SeatStatusUpdate.class);

    private String seatNumber;
    private boolean isBooked;

    /**
     * Default constructor.
     */
    public SeatStatusUpdate() {
        logger.debug("Created SeatStatusUpdate with default constructor");
    }

    /**
     * Constructs a SeatStatusUpdate with seat number and booking status.
     * Logs at info level and validates input.
     * @param seatNumber the seat number
     * @param isBooked the booking status
     */
    public SeatStatusUpdate(String seatNumber, boolean isBooked) {
        logger.info("Creating SeatStatusUpdate for seatNumber: {}, isBooked: {}", seatNumber, isBooked);
        if (seatNumber == null || seatNumber.isBlank()) {
            logger.error("Invalid seatNumber: {}", seatNumber);
            throw new IllegalArgumentException("Seat number must not be blank");
        }
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
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
     * Logs at debug level and validates input.
     * @param seatNumber seat number
     */
    public void setSeatNumber(String seatNumber) {
        logger.debug("Setting seatNumber: {}", seatNumber);
        if (seatNumber == null || seatNumber.isBlank()) {
            logger.error("Invalid seatNumber: {}", seatNumber);
            throw new IllegalArgumentException("Seat number must not be blank");
        }
        this.seatNumber = seatNumber;
    }

    /**
     * Gets the booking status.
     * @return true if booked, false otherwise
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     * Sets the booking status.
     * Logs at info level.
     * @param booked booking status
     */
    public void setBooked(boolean booked) {
        logger.info("Setting isBooked: {}", booked);
        this.isBooked = booked;
    }
}