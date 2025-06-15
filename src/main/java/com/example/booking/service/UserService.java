package com.example.booking.service;

import com.example.booking.model.User;
import com.example.booking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling user operations.
 * Provides methods for creating, updating, retrieving, and deleting users.
 * Includes logging and error handling for all operations.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users.
     *
     * @return list of all User entities
     */
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        try {
            List<User> users = userRepository.findAll();
            logger.debug("Found {} users", users.size());
            return users;
        } catch (Exception ex) {
            logger.error("Error fetching all users: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching all users", ex);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return Optional containing the User if found
     */
    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user by id={}", id);
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                logger.debug("User found: id={}", id);
            } else {
                logger.warn("User not found: id={}", id);
            }
            return user;
        } catch (Exception ex) {
            logger.error("Error fetching user by id: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching user by id", ex);
        }
    }

    /**
     * Creates a new user.
     *
     * @param user the User entity to create
     * @return the saved User entity
     */
    public User createUser(User user) {
        logger.info("Creating user: email={}", user.getEmail());
        try {
            User savedUser = userRepository.save(user);
            logger.debug("User created: id={}", savedUser.getId());
            return savedUser;
        } catch (Exception ex) {
            logger.error("Error creating user: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error creating user", ex);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param userDetails the updated user details
     * @return Optional containing the updated User if found
     */
    public Optional<User> updateUser(Long id, User userDetails) {
        logger.info("Updating user: id={}", id);
        try {
            return userRepository.findById(id).map(user -> {
                user.setName(userDetails.getName());
                user.setEmail(userDetails.getEmail());
                user.setCreatedAt(LocalDateTime.now());
                User updatedUser = userRepository.save(user);
                logger.debug("User updated: id={}", updatedUser.getId());
                return updatedUser;
            });
        } catch (Exception ex) {
            logger.error("Error updating user: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error updating user", ex);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID
     */
    public void deleteUser(Long id) {
        logger.info("Deleting user: id={}", id);
        try {
            userRepository.deleteById(id);
            logger.debug("User deleted: id={}", id);
        } catch (Exception ex) {
            logger.error("Error deleting user: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error deleting user", ex);
        }
    }
}