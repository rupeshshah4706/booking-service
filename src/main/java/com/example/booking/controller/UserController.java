package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users.
     *
     * @return list of all users
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        logger.info("Fetching all users");
        try {
            List<User> users = userService.getAllUsers();
            logger.debug("Found {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            logger.error("Error fetching all users", ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the user if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        try {
            return userService.getUserById(id)
                    .map(user -> {
                        logger.debug("User found: {}", user);
                        return ResponseEntity.ok(user);
                    })
                    .orElseGet(() -> {
                        logger.warn("User with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception ex) {
            logger.error("Error fetching user with id: {}", id, ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        logger.info("Creating user: {}", user);
        try {
            User created = userService.createUser(user);
            logger.debug("User created: {}", created);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            logger.error("User creation failed: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error creating user", ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id   the user ID
     * @param user the updated user data
     * @return the updated user or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with id: {}", id);
        try {
            return userService.updateUser(id, user)
                    .map(updated -> {
                        logger.debug("User updated: {}", updated);
                        return ResponseEntity.ok(updated);
                    })
                    .orElseGet(() -> {
                        logger.warn("User with id {} not found for update", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (RuntimeException ex) {
            logger.error("User update failed for id {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error updating user with id: {}", id, ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return 204 No Content if deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with id: {}", id);
        try {
            userService.deleteUser(id);
            logger.debug("User with id {} deleted", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            logger.error("User deletion failed for id {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error deleting user with id: {}", id, ex);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
}