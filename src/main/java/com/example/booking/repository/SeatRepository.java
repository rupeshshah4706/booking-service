// src/main/java/com/example/booking/repository/SeatRepository.java
package com.example.booking.repository;

import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Seat entities.
 * Provides methods to perform CRUD operations and custom queries on seats.
 * <p>
 * Note: Logging and exception handling should be implemented in the service or controller layers.
 * </p>
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * Finds a seat by event and seat number with a pessimistic write lock.
     *
     * @param event the event entity
     * @param seatNumber the seat number
     * @return an Optional containing the seat if found, or empty otherwise
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.event = :event AND s.seatNumber = :seatNumber")
    Optional<Seat> findByEventIdAndSeatNumberForUpdate(@Param("event") Event event, @Param("seatNumber") String seatNumber);

    /**
     * Finds a seat by event ID and seat number.
     *
     * @param event the event ID
     * @param seatNumber the seat number
     * @return an Optional containing the seat if found, or empty otherwise
     */
    Optional<Seat> findByEventIdAndSeatNumber(Long event, String seatNumber);

    /**
     * Finds all seats for a given event ID with pagination.
     *
     * @param eventId the event ID
     * @param pageable the pagination information
     * @return a page of seats for the event
     */
    Page<Seat> findByEventId(Long eventId, Pageable pageable);
}