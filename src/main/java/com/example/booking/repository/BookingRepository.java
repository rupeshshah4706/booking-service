package com.example.booking.repository;

import com.example.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Booking entities.
 * Provides methods to perform CRUD operations and custom queries on bookings.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds a booking by its ID and status.
     *
     * @param id the booking ID
     * @param status the booking status
     * @return an Optional containing the booking if found, or empty otherwise
     */
    Optional<Booking> findByIdAndStatus(Long id, String status);

    /**
     * Finds all bookings for a given user ID.
     *
     * @param userId the user ID
     * @return a list of bookings for the user
     */
    List<Booking> findByUserId(Long userId);

    /**
     * Finds a booking by event ID, seat ID, and status.
     *
     * @param eventId the event ID
     * @param seatId the seat ID
     * @param status the booking status
     * @return an Optional containing the booking if found, or empty otherwise
     */
    Optional<Booking> findByEventIdAndSeatIdAndStatus(Long eventId, Long seatId, String status);
}