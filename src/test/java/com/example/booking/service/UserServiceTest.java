package com.example.booking.service;

import com.example.booking.model.User;
import com.example.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 * Includes logging, error handling, and tests for user CRUD operations.
 */
class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logger.info("Mocks initialized for UserServiceTest");
    }

    /**
     * Test fetching all users successfully.
     */
    @Test
    void testGetAllUsers_Success() {
        logger.info("Running testGetAllUsers_Success");
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        logger.debug("Fetched {} users", result.size());
    }

    /**
     * Test fetching all users with exception.
     */
    @Test
    void testGetAllUsers_Exception() {
        logger.info("Running testGetAllUsers_Exception");
        when(userRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
        assertEquals("Error fetching all users", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test fetching user by ID successfully.
     */
    @Test
    void testGetUserById_Success() {
        logger.info("Running testGetUserById_Success");
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        logger.debug("User found: id={}", result.get().getId());
    }

    /**
     * Test fetching user by ID not found.
     */
    @Test
    void testGetUserById_NotFound() {
        logger.info("Running testGetUserById_NotFound");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        logger.warn("User not found: id=99");
    }

    /**
     * Test fetching user by ID with exception.
     */
    @Test
    void testGetUserById_Exception() {
        logger.info("Running testGetUserById_Exception");
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("Error fetching user by id", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test creating a user successfully.
     */
    @Test
    void testCreateUser_Success() {
        logger.info("Running testCreateUser_Success");
        User user = new User();
        user.setEmail("test@example.com");
        User savedUser = new User();
        savedUser.setId(1L);
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        logger.debug("User created: id={}", result.getId());
    }

    /**
     * Test creating a user with exception.
     */
    @Test
    void testCreateUser_Exception() {
        logger.info("Running testCreateUser_Exception");
        User user = new User();
        user.setEmail("fail@example.com");
        when(userRepository.save(user)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(user));
        assertEquals("Error creating user", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test updating a user successfully.
     */
    @Test
    void testUpdateUser_Success() {
        logger.info("Running testUpdateUser_Success");
        User user = new User();
        user.setId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        User userDetails = new User();
        userDetails.setName("New Name");
        userDetails.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> result = userService.updateUser(1L, userDetails);

        assertTrue(result.isPresent());
        logger.debug("User updated: id={}", result.get().getId());
    }

    /**
     * Test updating a user with exception.
     */
    @Test
    void testUpdateUser_Exception() {
        logger.info("Running testUpdateUser_Exception");
        User userDetails = new User();
        userDetails.setName("Name");
        userDetails.setEmail("email@example.com");

        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userDetails));
        assertEquals("Error updating user", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }

    /**
     * Test deleting a user successfully.
     */
    @Test
    void testDeleteUser_Success() {
        logger.info("Running testDeleteUser_Success");
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        logger.debug("User deleted: id=1");
    }

    /**
     * Test deleting a user with exception.
     */
    @Test
    void testDeleteUser_Exception() {
        logger.info("Running testDeleteUser_Exception");
        doThrow(new RuntimeException("DB error")).when(userRepository).deleteById(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("Error deleting user", ex.getMessage());
        logger.error("Expected exception: {}", ex.getMessage());
    }
}
