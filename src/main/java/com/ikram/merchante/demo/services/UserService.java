package com.ikram.merchante.demo.services;

import com.ikram.merchante.demo.entities.User;
import com.ikram.merchante.demo.infrastructures.VaultCredentialsManager;
import com.ikram.merchante.demo.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final int MAX_RETRIES = 3;

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return executeWithRetry(() -> userRepository.findById(id).orElse(null));
    }

    public List<User> getUsers() {
        return executeWithRetry(userRepository::findAll);
    }

    private <T> T executeWithRetry(Supplier<T> operation) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return operation.get();
            } catch (InvalidDataAccessResourceUsageException e) {
                logger.warn("Database access error on attempt {}/{}. Triggering credential rotation.", attempt, MAX_RETRIES);
                VaultCredentialsManager.loadAndRefreshContextCredentials();
            }
        }

        throw new IllegalStateException("Database access failed after " + MAX_RETRIES + " attempts.");
    }
}
