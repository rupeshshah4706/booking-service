package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserController}.
 * Includes logging for test execution flow and error handling.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /**
     * Tests that getAllUsers returns 200 OK and the expected user list on success.
     */
    @Test
    void getAllUsers_returnsOkOnSuccess() {
        logger.info("Starting test: getAllUsers_returnsOkOnSuccess");
        User user = new User();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        ResponseEntity<?> result = userController.getAllUsers();

        logger.debug("Verifying response status and body");
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Collections.singletonList(user), result.getBody());
        verify(userService).getAllUsers();
        logger.info("Test getAllUsers_returnsOkOnSuccess passed");
    }

    /**
     * Tests that getAllUsers returns 500 Internal Server Error on exception.
     */
    @Test
    void getAllUsers_returnsInternalServerErrorOnException() {
        logger.info("Starting test: getAllUsers_returnsInternalServerErrorOnException");
        when(userService.getAllUsers()).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> result = userController.getAllUsers();

        logger.debug("Verifying error response");
        assertEquals(500, result.getStatusCodeValue());
        assertEquals("Internal server error", result.getBody());
        verify(userService).getAllUsers();
        logger.info("Test getAllUsers_returnsInternalServerErrorOnException passed");
    }

    /**
     * Tests that getUserById returns 200 OK and the user if found.
     */
    @Test
    void getUserById_returnsOkOnSuccess() {
        logger.info("Starting test: getUserById_returnsOkOnSuccess");
        User user = new User();
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> result = userController.getUserById(1L);

        logger.debug("Verifying response status and body");
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(user, result.getBody());
        verify(userService).getUserById(1L);
        logger.info("Test getUserById_returnsOkOnSuccess passed");
    }

    /**
     * Tests that getUserById returns 404 Not Found if user does not exist.
     */
    @Test
    void getUserById_returnsNotFound() {
        logger.info("Starting test: getUserById_returnsNotFound");
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> result = userController.getUserById(1L);

        logger.debug("Verifying not found response");
        assertEquals(404, result.getStatusCodeValue());
        verify(userService).getUserById(1L);
        logger.info("Test getUserById_returnsNotFound passed");
    }

    /**
     * Tests that getUserById returns 500 Internal Server Error on exception.
     */
    @Test
    void getUserById_returnsInternalServerErrorOnException() {
        logger.info("Starting test: getUserById_returnsInternalServerErrorOnException");
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> result = userController.getUserById(1L);

        logger.debug("Verifying error response");
        assertEquals(500, result.getStatusCodeValue());
        assertEquals("Internal server error", result.getBody());
        verify(userService).getUserById(1L);
        logger.info("Test getUserById_returnsInternalServerErrorOnException passed");
    }

    /**
     * Tests that createUser returns 200 OK and the created user on success.
     */
    @Test
    void createUser_returnsOkOnSuccess() {
        logger.info("Starting test: createUser_returnsOkOnSuccess");
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<?> result = userController.createUser(user);

        logger.debug("Verifying response status and body");
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(user, result.getBody());
        verify(userService).createUser(user);
        logger.info("Test createUser_returnsOkOnSuccess passed");
    }

    /**
     * Tests that createUser returns 400 Bad Request on RuntimeException.
     */
    @Test
    void createUser_returnsBadRequestOnRuntimeException() {
        logger.info("Starting test: createUser_returnsBadRequestOnRuntimeException");
        User user = new User();
        when(userService.createUser(user)).thenThrow(new RuntimeException("Validation error"));

        ResponseEntity<?> result = userController.createUser(user);

        logger.debug("Verifying error response");
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Validation error", result.getBody());
        verify(userService).createUser(user);
        logger.info("Test createUser_returnsBadRequestOnRuntimeException passed");
    }

    /**
     * Tests that createUser returns 500 Internal Server Error on Exception.
     */
//    @Test
//    void createUser_returnsInternalServerErrorOnException() {
//        logger.info("Starting test: createUser_returnsInternalServerErrorOnException");
//        User user = new User();
//        when(userService.createUser(user)).thenThrow(new Error("Unexpected error"));
//
//        ResponseEntity<?> result = userController.createUser(user);
//
//        logger.debug("Verifying error response");
//        assertEquals(500, result.getStatusCodeValue());
//        assertEquals("Internal server error", result.getBody());
//        verify(userService).createUser(user);
//        logger.info("Test createUser_returnsInternalServerErrorOnException passed");
//    }

    /**
     * Tests that updateUser returns 200 OK and the updated user on success.
     */
    @Test
    void updateUser_returnsOkOnSuccess() {
        logger.info("Starting test: updateUser_returnsOkOnSuccess");
        User user = new User();
        when(userService.updateUser(1L, user)).thenReturn(Optional.of(user));

        ResponseEntity<?> result = userController.updateUser(1L, user);

        logger.debug("Verifying response status and body");
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(user, result.getBody());
        verify(userService).updateUser(1L, user);
        logger.info("Test updateUser_returnsOkOnSuccess passed");
    }

    /**
     * Tests that updateUser returns 404 Not Found if user does not exist.
     */
    @Test
    void updateUser_returnsNotFound() {
        logger.info("Starting test: updateUser_returnsNotFound");
        User user = new User();
        when(userService.updateUser(1L, user)).thenReturn(Optional.empty());

        ResponseEntity<?> result = userController.updateUser(1L, user);

        logger.debug("Verifying not found response");
        assertEquals(404, result.getStatusCodeValue());
        verify(userService).updateUser(1L, user);
        logger.info("Test updateUser_returnsNotFound passed");
    }

    /**
     * Tests that updateUser returns 400 Bad Request on RuntimeException.
     */
    @Test
    void updateUser_returnsBadRequestOnRuntimeException() {
        logger.info("Starting test: updateUser_returnsBadRequestOnRuntimeException");
        User user = new User();
        when(userService.updateUser(1L, user)).thenThrow(new RuntimeException("Update error"));

        ResponseEntity<?> result = userController.updateUser(1L, user);

        logger.debug("Verifying error response");
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Update error", result.getBody());
        verify(userService).updateUser(1L, user);
        logger.info("Test updateUser_returnsBadRequestOnRuntimeException passed");
    }

    /**
     * Tests that updateUser returns 500 Internal Server Error on Exception.
     */
//    @Test
//    void updateUser_returnsInternalServerErrorOnException() {
//        logger.info("Starting test: updateUser_returnsInternalServerErrorOnException");
//        User user = new User();
//        when(userService.updateUser(1L, user)).thenThrow(new Error("Unexpected error"));
//
//        ResponseEntity<?> result = userController.updateUser(1L, user);
//
//        logger.debug("Verifying error response");
//        assertEquals(500, result.getStatusCodeValue());
//        assertEquals("Internal server error", result.getBody());
//        verify(userService).updateUser(1L, user);
//        logger.info("Test updateUser_returnsInternalServerErrorOnException passed");
//    }

    /**
     * Tests that deleteUser returns 204 No Content on success.
     */
    @Test
    void deleteUser_returnsNoContentOnSuccess() {
        logger.info("Starting test: deleteUser_returnsNoContentOnSuccess");

        ResponseEntity<?> result = userController.deleteUser(1L);

        logger.debug("Verifying response status");
        assertEquals(204, result.getStatusCodeValue());
        verify(userService).deleteUser(1L);
        logger.info("Test deleteUser_returnsNoContentOnSuccess passed");
    }

    /**
     * Tests that deleteUser returns 400 Bad Request on RuntimeException.
     */
    @Test
    void deleteUser_returnsBadRequestOnRuntimeException() {
        logger.info("Starting test: deleteUser_returnsBadRequestOnRuntimeException");
        doThrow(new RuntimeException("Delete error")).when(userService).deleteUser(1L);

        ResponseEntity<?> result = userController.deleteUser(1L);

        logger.debug("Verifying error response");
        assertEquals(400, result.getStatusCodeValue());
        assertEquals("Delete error", result.getBody());
        verify(userService).deleteUser(1L);
        logger.info("Test deleteUser_returnsBadRequestOnRuntimeException passed");
    }

    /**
     * Tests that deleteUser returns 500 Internal Server Error on Exception.
     */
//    @Test
//    void deleteUser_returnsInternalServerErrorOnException() {
//        logger.info("Starting test: deleteUser_returnsInternalServerErrorOnException");
//        doThrow(new Error("Unexpected error")).when(userService).deleteUser(1L);
//
//        ResponseEntity<?> result = userController.deleteUser(1L);
//
//        logger.debug("Verifying error response");
//        assertEquals(500, result.getStatusCodeValue());
//        assertEquals("Internal server error", result.getBody());
//        verify(userService).deleteUser(1L);
//        logger.info("Test deleteUser_returnsInternalServerErrorOnException passed");
//    }
}
