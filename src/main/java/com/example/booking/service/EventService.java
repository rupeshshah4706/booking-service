// src/main/java/com/example/booking/service/EventService.java
package com.example.booking.service;

import com.example.booking.model.EventRequest;
import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import com.example.booking.repository.EventRepository;
import com.example.booking.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling event operations.
 * Provides methods for creating, updating, retrieving, and deleting events,
 * as well as managing seats for events.
 */
@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the event ID
     * @return the Event entity
     * @throws RuntimeException if the event is not found
     */
    public Event getEvent(Long eventId) {
        logger.info("Fetching event with id={}", eventId);
        try {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> {
                        logger.error("Event not found: id={}", eventId);
                        return new RuntimeException("Event not found");
                    });
        } catch (Exception ex) {
            logger.error("Error fetching event: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching event", ex);
        }
    }

    /**
     * Retrieves paginated seats for a given event.
     *
     * @param eventId the event ID
     * @param pageable pagination information
     * @return a page of Seat entities
     */
    public Page<Seat> getSeatsForEvent(Long eventId, Pageable pageable) {
        logger.info("Fetching seats for eventId={}", eventId);
        try {
            Page<Seat> seats = seatRepository.findByEventId(eventId, pageable);
            logger.debug("Found {} seats for eventId={}", seats.getTotalElements(), eventId);
            return seats;
        } catch (Exception ex) {
            logger.error("Error fetching seats for event: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching seats for event", ex);
        }
    }

    /**
     * Creates a new event and its associated seats.
     *
     * @param request the event creation request
     * @return the saved Event entity
     */
    @Transactional
    public Event createEventWithSeats(EventRequest request) {
        logger.info("Creating event: name={}, location={}", request.getName(), request.getLocation());
        try {
            Event event = new Event();
            event.setName(request.getName());
            event.setLocation(request.getLocation());
            event.setEventDate(request.getEventDate());
            event.setTotalSeats(request.getTotalSeats());
            Event savedEvent = eventRepository.save(event);
            logger.debug("Event created with id={}", savedEvent.getId());

            for (int i = request.getSeatStart(); i <= request.getSeatEnd(); i++) {
                Seat seat = new Seat();
                seat.setEvent(savedEvent);
                seat.setSeatNumber(String.valueOf(i));
                seat.setIsBooked(false);
                seatRepository.save(seat);
            }
            logger.info("Created {} seats for eventId={}", request.getSeatEnd() - request.getSeatStart() + 1, savedEvent.getId());
            return savedEvent;
        } catch (Exception ex) {
            logger.error("Error creating event with seats: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error creating event with seats", ex);
        }
    }

    /**
     * Retrieves all events.
     *
     * @return list of all Event entities
     */
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        try {
            List<Event> events = eventRepository.findAll();
            logger.debug("Found {} events", events.size());
            return events;
        } catch (Exception ex) {
            logger.error("Error fetching all events: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching all events", ex);
        }
    }

    /**
     * Retrieves an event by its ID as an Optional.
     *
     * @param id the event ID
     * @return Optional containing the Event if found
     */
    public Optional<Event> getEventById(Long id) {
        logger.info("Fetching event by id={}", id);
        try {
            Optional<Event> event = eventRepository.findById(id);
            if (event.isPresent()) {
                logger.debug("Event found: id={}", id);
            } else {
                logger.warn("Event not found: id={}", id);
            }
            return event;
        } catch (Exception ex) {
            logger.error("Error fetching event by id: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching event by id", ex);
        }
    }

    /**
     * Updates an existing event.
     *
     * @param id the event ID
     * @param request the event update request
     * @return Optional containing the updated Event if found
     */
    @Transactional
    public Optional<Event> updateEvent(Long id, EventRequest request) {
        logger.info("Updating event: id={}", id);
        try {
            return eventRepository.findById(id).map(event -> {
                event.setName(request.getName());
                event.setLocation(request.getLocation());
                event.setEventDate(request.getEventDate());
                event.setTotalSeats(request.getTotalSeats());
                Event updatedEvent = eventRepository.save(event);
                logger.debug("Event updated: id={}", updatedEvent.getId());
                return updatedEvent;
            });
        } catch (Exception ex) {
            logger.error("Error updating event: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error updating event", ex);
        }
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id the event ID
     */
    @Transactional
    public void deleteEvent(Long id) {
        logger.info("Deleting event: id={}", id);
        try {
                // Delete seats first
                seatRepository.deleteAll(seatRepository.findByEventId(id, Pageable.unpaged()));
                // Then delete event
                eventRepository.deleteById(id);
                logger.debug("Event and its seats deleted: id={}", id);
        } catch (Exception ex) {
            logger.error("Error deleting event: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error deleting event", ex);
        }
    }
}