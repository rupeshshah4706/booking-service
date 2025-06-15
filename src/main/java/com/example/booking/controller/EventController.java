// src/main/java/com/example/booking/controller/EventController.java
package com.example.booking.controller;

import com.example.booking.model.EventRequest;
import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import com.example.booking.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.booking.model.EventWithSeats;

import java.util.List;

/**
 * REST controller for managing events and their associated seats.
 * Provides endpoints for creating, retrieving, updating, and deleting events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    /**
     * Creates a new event with the provided details.
     *
     * @param request The request body containing event details.
     * @return The created event.
     */
    @PostMapping
    public Event createEvent(@RequestBody EventRequest request) {
        logger.info("Creating event with request: {}", request);
        try {
            Event event = eventService.createEventWithSeats(request);
            logger.debug("Event created successfully: {}", event);
            return event;
        } catch (Exception e) {
            logger.error("Error creating event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create event", e);
        }
    }

    /**
     * Retrieves all events.
     *
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        try {
            List<Event> events = eventService.getAllEvents();
            logger.debug("Fetched {} events", events.size());
            return events;
        } catch (Exception e) {
            logger.error("Error fetching all events: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch events", e);
        }
    }

    /**
     * Retrieves an event along with its paginated seats.
     *
     * @param id The ID of the event to retrieve.
     * @param pageable The pagination information.
     * @return The event and its paginated seats.
     */
    @GetMapping("/{id}")
    public EventWithSeats getEventWithSeats(
            @PathVariable Long id,
            Pageable pageable
    ) {
        logger.info("Fetching event with id: {} and its seats", id);
        try {
            Event event = eventService.getEvent(id);
            Page<Seat> seats = eventService.getSeatsForEvent(id, pageable);
            logger.debug("Fetched event: {} with {} seats", event, seats.getTotalElements());
            return new EventWithSeats(event, seats);
        } catch (Exception e) {
            logger.error("Error fetching event with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch event with seats", e);
        }
    }

    /**
     * Updates an existing event with the provided details.
     *
     * @param id The ID of the event to update.
     * @param request The request body containing updated event details.
     * @return The updated event, or a 404 response if the event is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        logger.info("Updating event with id: {}", id);
        try {
            return eventService.updateEvent(id, request)
                    .map(event -> {
                        logger.debug("Event updated successfully: {}", event);
                        return ResponseEntity.ok(event);
                    })
                    .orElseGet(() -> {
                        logger.error("Event with id {} not found for update", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error updating event with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update event", e);
        }
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id The ID of the event to delete.
     * @return A 204 No Content response if the deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Deleting event with id: {}", id);
        try {
            eventService.deleteEvent(id);
            logger.debug("Event deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting event with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete event", e);
        }
    }
}