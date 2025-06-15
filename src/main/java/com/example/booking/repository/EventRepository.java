// src/main/java/com/example/booking/repository/EventRepository.java
package com.example.booking.repository;

import com.example.booking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for Event entities.
 * Provides methods to perform CRUD operations and custom queries on events.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Finds an event by its name.
     *
     * @param name the event name
     * @return an Optional containing the event if found, or empty otherwise
     */
    Optional<Event> findByName(String name);

//    /**
//     * Finds all events by their status.
//     *
//     * @param status the event status
//     * @return a list of events with the given status
//     */
//    List<Event> findByStatus(String status);
}