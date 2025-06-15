package com.example.booking.repository;

import com.example.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * Provides methods to perform CRUD operations and custom queries on users.
 * <p>
 * Note: Logging and exception handling should be implemented in the service or controller layers.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the user's email
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);
}