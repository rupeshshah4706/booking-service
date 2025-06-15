package com.example.booking.controller;

import com.example.booking.model.EventRequest;
import com.example.booking.model.Event;
import com.example.booking.model.Seat;
import com.example.booking.model.EventWithSeats;
import com.example.booking.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link com.example.booking.controller.EventController}.
 * Tests both successful and exceptional scenarios for each endpoint.
 */
@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    /**
     * Should create an event successfully when the service returns an event.
     */
    @Test
    void createEvent_createsEventSuccessfully() {
        // Arrange
        EventRequest request = new EventRequest();
        Event event = new Event();
        when(eventService.createEventWithSeats(request)).thenReturn(event);

        // Act
        Event result = eventController.createEvent(request);

        // Assert
        assertEquals(event, result);
        verify(eventService).createEventWithSeats(request);
    }

    /**
     * Should throw a RuntimeException when the service fails to create an event.
     */
    @Test
    void createEvent_throwsExceptionWhenServiceFails() {
        // Arrange
        EventRequest request = new EventRequest();
        when(eventService.createEventWithSeats(request)).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventController.createEvent(request));
        verify(eventService).createEventWithSeats(request);
    }

    /**
     * Should return an event with paginated seats when both are found.
     */
    @Test
    void getEventWithSeats_returnsEventWithPaginatedSeats() {
        // Arrange
        Long eventId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Event event = new Event();
        Page<Seat> seats = new PageImpl<>(List.of(new Seat()));
        when(eventService.getEvent(eventId)).thenReturn(event);
        when(eventService.getSeatsForEvent(eventId, pageable)).thenReturn(seats);

        // Act
        EventWithSeats result = eventController.getEventWithSeats(eventId, pageable);

        // Assert
        assertEquals(event, result.getEvent());
        assertEquals(seats, result.getSeats());
        verify(eventService).getEvent(eventId);
        verify(eventService).getSeatsForEvent(eventId, pageable);
    }

    /**
     * Should throw a RuntimeException when the event is not found.
     */
    @Test
    void getEventWithSeats_throwsExceptionWhenEventNotFound() {
        // Arrange
        Long eventId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        when(eventService.getEvent(eventId)).thenThrow(new RuntimeException("Event not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventController.getEventWithSeats(eventId, pageable));
        verify(eventService).getEvent(eventId);
        verify(eventService, never()).getSeatsForEvent(anyLong(), any(Pageable.class));
    }

    /**
     * Should update an event successfully and return HTTP 200.
     */
    @Test
    void updateEvent_updatesEventSuccessfully() {
        // Arrange
        Long eventId = 1L;
        EventRequest request = new EventRequest();
        Event updatedEvent = new Event();
        when(eventService.updateEvent(eventId, request)).thenReturn(Optional.of(updatedEvent));

        // Act
        ResponseEntity<Event> response = eventController.updateEvent(eventId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEvent, response.getBody());
        verify(eventService).updateEvent(eventId, request);
    }

    /**
     * Should return HTTP 404 when trying to update a non-existent event.
     */
    @Test
    void updateEvent_returnsNotFoundWhenEventDoesNotExist() {
        // Arrange
        Long eventId = 1L;
        EventRequest request = new EventRequest();
        when(eventService.updateEvent(eventId, request)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Event> response = eventController.updateEvent(eventId, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventService).updateEvent(eventId, request);
    }

    /**
     * Should delete an event successfully and return HTTP 204.
     */
    @Test
    void deleteEvent_deletesEventSuccessfully() {
        // Arrange
        Long eventId = 1L;

        // Act
        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService).deleteEvent(eventId);
    }

    /**
     * Should throw a RuntimeException when the service fails to delete an event.
     */
    @Test
    void deleteEvent_throwsExceptionWhenServiceFails() {
        // Arrange
        Long eventId = 1L;
        doThrow(new RuntimeException("Service error")).when(eventService).deleteEvent(eventId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventController.deleteEvent(eventId));
        verify(eventService).deleteEvent(eventId);
    }
}
