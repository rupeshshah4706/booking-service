package com.example.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Booking Service Spring Boot application.
 * Handles application startup with logging and error handling.
 */
@SpringBootApplication
public class BookingServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceApplication.class);

	/**
	 * Starts the Booking Service application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		logger.info("Starting BookingServiceApplication...");
		try {
			SpringApplication.run(BookingServiceApplication.class, args);
			logger.debug("BookingServiceApplication started successfully.");
		} catch (Exception ex) {
			logger.error("Error starting BookingServiceApplication: {}", ex.getMessage(), ex);
			throw ex;
		}
	}
}
