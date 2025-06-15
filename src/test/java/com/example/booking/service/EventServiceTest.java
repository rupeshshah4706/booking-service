package com.example.booking.service;

import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import com.example.booking.model.EventRequest;
import com.example.booking.repository.EventRepository;
import com.example.booking.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EventService.
 * Includes logging, error handling, and tests for event CRUD and seat management.
 */
class EventServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceTest.class);

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logger.info("Mocks initialized for EventServiceTest");
    }

    /**
     * Test fetching an event by ID successfully.
     */
    @Test
    void testGetEvent_Success() {
        logger.info("Running testGetEvent_Success");
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getEvent(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        logger.debug("Fetched event with id={}", result.getId());
    }

    /**
     * Test fetching an event by ID when not found.
     */
//    @Test
//    void testGetEvent_NotFound() {
//        logger.info("Running testGetEvent_NotFound");
//        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> eventService.getEvent(99L));
//        assertEquals("Event not found", ex.getMessage());
//        logger.error("Expected exception: {}", ex.getMessage());
//    }

    /**
     * Test fetching paginated seats for an event.
     */
    @Test
    void testGetSeatsForEvent() {
        logger.info("Running testGetSeatsForEvent");
        List<Seat> seatList = Arrays.asList(new Seat(), new Seat());
        Page<Seat> seatPage = new PageImpl<>(seatList);
        when(seatRepository.findByEventId(eq(1L), any(Pageable.class))).thenReturn(seatPage);

        Page<Seat> result = eventService.getSeatsForEvent(1L, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        logger.debug("Fetched {} seats for eventId=1", result.getTotalElements());
    }

    /**
     * Test creating an event with seats.
     */
    @Test
    void testCreateEventWithSeats() {
        logger.info("Running testCreateEventWithSeats");
        EventRequest request = new EventRequest();
        request.setName("Test Event");
        request.setLocation("Test Location");
        request.setEventDate(LocalDateTime.MAX);
        request.setTotalSeats(2);
        request.setSeatStart(1);
        request.setSeatEnd(2);

        Event event = new Event();
        event.setId(1L);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(seatRepository.save(any(Seat.class))).thenReturn(new Seat());

        Event result = eventService.createEventWithSeats(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        logger.debug("Created event with id={}", result.getId());
    }

    /**
     * Test fetching all events.
     */
    @Test
    void testGetAllEvents() {
        logger.info("Running testGetAllEvents");
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        logger.debug("Fetched {} events", result.size());
    }

    /**
     * Test fetching event by ID as Optional.
     */
    @Test
    void testGetEventById() {
        logger.info("Running testGetEventById");
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.getEventById(1L);

        assertTrue(result.isPresent());
        logger.debug("Event found: id={}", result.get().getId());
    }

    /**
     * Test updating an event.
     */
    @Test
    void testUpdateEvent() {
        logger.info("Running testUpdateEvent");
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventRequest request = new EventRequest();
        request.setName("Updated");
        request.setLocation("Updated");
        request.setEventDate(LocalDateTime.MAX);
        request.setTotalSeats(100);

        Optional<Event> result = eventService.updateEvent(1L, request);

        assertTrue(result.isPresent());
        logger.debug("Event updated: id={}", result.get().getId());
    }

    /**
     * Test deleting an event.
     */
    @Test
    void testDeleteEvent() {
        logger.info("Running testDeleteEvent");
        doNothing().when(eventRepository).deleteById(1L);

        assertDoesNotThrow(() -> eventService.deleteEvent(1L));
        logger.debug("Event deleted: id=1");
    }
}
