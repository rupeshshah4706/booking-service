package com.example.booking.service;

import com.example.booking.model.*;
import com.example.booking.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookingService.
 * Includes logging, error handling, and tests for booking, cancellation, and retrieval.
 */
class BookingServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logger.info("Mocks initialized for BookingServiceTest");
    }

    /**
     * Test successful ticket booking.
     */
    @Test
    void testBookTicket_Success() {
        logger.info("Running testBookTicket_Success");
        BookingRequest request = new BookingRequest();
        request.setUserId(1L);
        request.setEventId(2L);
        request.setSeatNumber("A1");
        Event event = new Event();
        event.setId(2L);
        event.setTotalSeats(10);
        Seat seat = new Seat();
        seat.setId(3L);
        seat.setSeatNumber("A1");
        seat.setIsBooked(false);
        seat.setEvent(event);

        when(eventRepository.findById(2L)).thenReturn(Optional.of(event));
        when(seatRepository.findByEventIdAndSeatNumberForUpdate(event, "A1")).thenReturn(Optional.of(seat));
        when(bookingRepository.findByEventIdAndSeatIdAndStatus(2L, 3L, "BOOKED")).thenReturn(Optional.empty());
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(100L);
            return b;
        });

        Booking booking = bookingService.bookTicket(request);

        assertNotNull(booking);
        assertEquals("BOOKED", booking.getStatus());
        logger.debug("Booking created with id={}", booking.getId());
        verify(kafkaTemplate).send(anyString(), contains("BOOKED"));
    }

    /**
     * Test booking when event is not found.
     */
    @Test
    void testBookTicket_EventNotFound() {
        logger.info("Running testBookTicket_EventNotFound");
        BookingRequest request = new BookingRequest();
        request.setUserId(1L);
        request.setEventId(99L);
        request.setSeatNumber("A1");
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.bookTicket(request));
        assertEquals("Event not found", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test booking when seat is already booked.
     */
    @Test
    void testBookTicket_SeatAlreadyBooked() {
        logger.info("Running testBookTicket_SeatAlreadyBooked");
        BookingRequest request = new BookingRequest();
        request.setUserId(1L);
        request.setEventId(2L);
        request.setSeatNumber("A1");
        Event event = new Event();
        event.setId(2L);
        event.setTotalSeats(10);
        Seat seat = new Seat();
        seat.setId(3L);
        seat.setSeatNumber("A1");
        seat.setIsBooked(true);
        seat.setEvent(event);

        when(eventRepository.findById(2L)).thenReturn(Optional.of(event));
        when(seatRepository.findByEventIdAndSeatNumberForUpdate(event, "A1")).thenReturn(Optional.of(seat));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.bookTicket(request));
        assertEquals("Seat already booked", ex.getMessage());
        logger.warn("Expected warning: {}", ex.getMessage());
    }

    /**
     * Test successful booking cancellation.
     */
    @Test
    void testCancelBooking_Success() {
        logger.info("Running testCancelBooking_Success");
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStatus("BOOKED");
        booking.setSeatId(3L);
        booking.setEventId(2L);
        booking.setUserId(1L);

        Seat seat = new Seat();
        seat.setId(3L);
        seat.setSeatNumber("A1");
        seat.setIsBooked(true);

        when(bookingRepository.findByIdAndStatus(10L, "BOOKED")).thenReturn(Optional.of(booking));
        when(seatRepository.findById(3L)).thenReturn(Optional.of(seat));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        assertDoesNotThrow(() -> bookingService.cancelBooking(10L));
        logger.debug("Booking cancelled for id={}", booking.getId());
        verify(kafkaTemplate).send(anyString(), contains("CANCELLED"));
    }

    /**
     * Test cancellation when booking is not found.
     */
    @Test
    void testCancelBooking_BookingNotFound() {
        logger.info("Running testCancelBooking_BookingNotFound");
        when(bookingRepository.findByIdAndStatus(99L, "BOOKED")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(99L));
        assertEquals("Active booking not found", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test fetching user bookings.
     */
    @Test
    void testGetUserBookings() {
        logger.info("Running testGetUserBookings");
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepository.findByUserId(1L)).thenReturn(bookings);

        List<Booking> result = bookingService.getUserBookings(1L);

        assertEquals(2, result.size());
        logger.debug("Fetched {} bookings for userId=1", result.size());
    }
}
