package com.example.booking.controller;

import com.example.booking.model.BookingRequest;
import com.example.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing booking operations.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    /**
     * Books a ticket based on the provided booking request.
     *
     * @param request the booking request details
     * @return the booking confirmation or error message
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        logger.info("Received booking request: {}", request);
        try {
            Object response = bookingService.bookTicket(request);
            logger.debug("Booking successful: {}", response);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            logger.error("Booking failed: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error during booking", ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Cancels a booking with the given booking ID.
     *
     * @param bookingId the ID of the booking to cancel
     * @return success message or error message
     */
    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        logger.info("Received cancel request for bookingId: {}", bookingId);
        try {
            bookingService.cancelBooking(bookingId);
            logger.debug("Booking {} cancelled successfully", bookingId);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (RuntimeException ex) {
            logger.error("Cancellation failed for bookingId {}: {}", bookingId, ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error during cancellation for bookingId {}", bookingId, ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the ID of the user
     * @return list of bookings
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        logger.info("Fetching bookings for userId: {}", userId);
        try {
            Object bookings = bookingService.getUserBookings(userId);
            logger.debug("Found bookings for userId {}: {}", userId, bookings);
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException ex) {
            logger.error("Failed to fetch bookings for userId {}: {}", userId, ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error fetching bookings for userId {}", userId, ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
}