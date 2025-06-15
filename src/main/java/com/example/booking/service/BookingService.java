package com.example.booking.service;

import com.example.booking.model.*;
import com.example.booking.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for handling booking operations.
 * Includes booking, cancellation, and retrieval of user bookings.
 * Provides logging and error handling for all operations.
 */
@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public static final String TOPIC = "booking-events";

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Books a ticket for a user for a specific event and seat.
     * Handles seat locking, booking creation, event emission, and client notification.
     *
     * @param request the booking request
     * @return the saved Booking entity
     * @throws RuntimeException if booking fails due to invalid state or data
     */
    @Transactional
    public Booking bookTicket(BookingRequest request) {
        logger.info("Attempting to book ticket: userId={}, eventId={}, seatNumber={}",
                request.getUserId(), request.getEventId(), request.getSeatNumber());
        try {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> {
                        logger.error("Event not found: eventId={}", request.getEventId());
                        return new RuntimeException("Event not found");
                    });

            if (event.getTotalSeats() <= 0) {
                logger.warn("No seats available for event: eventId={}", event.getId());
                throw new RuntimeException("No seats available for this event");
            }

            Seat seat = seatRepository.findByEventIdAndSeatNumberForUpdate(event, request.getSeatNumber())
                    .orElseThrow(() -> {
                        logger.error("Seat not found: eventId={}, seatNumber={}", event.getId(), request.getSeatNumber());
                        return new RuntimeException("Seat not found");
                    });

            if (Boolean.TRUE.equals(seat.getIsBooked())) {
                logger.warn("Seat already booked: seatId={}, seatNumber={}", seat.getId(), seat.getSeatNumber());
                throw new RuntimeException("Seat already booked");
            }

            if (bookingRepository.findByEventIdAndSeatIdAndStatus(seat.getEvent().getId(), seat.getId(), "BOOKED").isPresent()) {
                logger.warn("Booking already exists for seat: seatId={}, eventId={}", seat.getId(), event.getId());
                throw new RuntimeException("Booking already exists for this seat");
            }

            seat.setIsBooked(true);
            seatRepository.save(seat);
            logger.debug("Seat marked as booked: seatId={}", seat.getId());

            Booking booking = new Booking();
            booking.setUserId(request.getUserId());
            booking.setEventId(request.getEventId());
            booking.setSeatId(seat.getId());
            booking.setStatus("BOOKED");
            booking.setBookedAt(java.time.LocalDateTime.now());
            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking created: bookingId={}", savedBooking.getId());

            String message = String.format("{\"type\":\"BOOKED\",\"bookingId\":%d,\"userId\":%d,\"eventId\":%d,\"seatNumber\":\"%s\"}",
                    savedBooking.getId(), savedBooking.getUserId(), event.getId(), seat.getSeatNumber());
            kafkaTemplate.send(TOPIC, message);
            logger.debug("Booking event sent to Kafka: {}", message);

            messagingTemplate.convertAndSend(
                    "/topic/seats/" + event.getId(),
                    new SeatStatusUpdate(seat.getSeatNumber(), true)
            );
            logger.debug("WebSocket notification sent for seat booking: eventId={}, seatNumber={}", event.getId(), seat.getSeatNumber());

            return savedBooking;
        } catch (RuntimeException ex) {
            logger.error("Error booking ticket: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during booking: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error during booking", ex);
        }
    }

    /**
     * Cancels an active booking and frees up the seat.
     * Emits cancellation events and notifies clients.
     *
     * @param bookingId the ID of the booking to cancel
     * @throws RuntimeException if cancellation fails due to invalid state or data
     */
    @Transactional
    public void cancelBooking(Long bookingId) {
        logger.info("Attempting to cancel booking: bookingId={}", bookingId);
        try {
            Booking booking = bookingRepository.findByIdAndStatus(bookingId, "BOOKED")
                    .orElseThrow(() -> {
                        logger.error("Active booking not found: bookingId={}", bookingId);
                        return new RuntimeException("Active booking not found");
                    });

            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            logger.info("Booking marked as cancelled: bookingId={}", booking.getId());

            Seat seat = seatRepository.findById(booking.getSeatId())
                    .orElseThrow(() -> {
                        logger.error("Seat not found for cancelled booking: seatId={}", booking.getSeatId());
                        return new RuntimeException("Seat not found");
                    });
            seat.setIsBooked(false);
            seatRepository.save(seat);
            logger.debug("Seat marked as available: seatId={}", seat.getId());

            String message = String.format("{\"type\":\"CANCELLED\",\"bookingId\":%d,\"userId\":%d,\"eventId\":%d,\"seatNumber\":\"%s\"}",
                    booking.getId(), booking.getUserId(), booking.getEventId(), seat.getSeatNumber());
            kafkaTemplate.send(TOPIC, message);
            logger.debug("Cancellation event sent to Kafka: {}", message);

            messagingTemplate.convertAndSend(
                    "/topic/seats/" + booking.getEventId(),
                    new SeatStatusUpdate(seat.getSeatNumber(), false)
            );
            logger.debug("WebSocket notification sent for seat cancellation: eventId={}, seatNumber={}", booking.getEventId(), seat.getSeatNumber());

            bookingRepository.delete(booking);
            logger.info("Cancelled booking deleted: bookingId={}", booking.getId());
        } catch (RuntimeException ex) {
            logger.error("Error cancelling booking: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during cancellation: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error during cancellation", ex);
        }
    }

    /**
     * Retrieves all bookings for a given user.
     *
     * @param userId the user ID
     * @return list of bookings for the user
     */
    public List<Booking> getUserBookings(Long userId) {
        logger.info("Fetching bookings for user: userId={}", userId);
        try {
            List<Booking> bookings = bookingRepository.findByUserId(userId);
            logger.debug("Found {} bookings for userId={}", bookings.size(), userId);
            return bookings;
        } catch (Exception ex) {
            logger.error("Error fetching user bookings: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching user bookings", ex);
        }
    }
}