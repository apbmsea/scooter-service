package test.weewee.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.weewee.userservice.exception.UserNotFoundException;
import test.weewee.userservice.model.User;
import test.weewee.userservice.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public User createUser(User user) {
        log.debug("Creating new user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        log.debug("Finding user by phone: {}", phone);
        return userRepository.findByPhone(phone);
    }

    public void deleteUser(UUID id) {
        log.debug("Deleting user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        
        String email = user.getEmail();
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
        
        // Отправляем событие в RabbitMQ
        userEventProducer.sendUserDeletedEvent(id, email);
    }

    public void deleteUserByEmail(String email) {
        log.debug("Deleting user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        UUID userId = user.getId();
        userRepository.delete(user);
        log.info("User deleted successfully with email: {}", email);
        
        // Отправляем событие в RabbitMQ
        userEventProducer.sendUserDeletedEvent(userId, email);
    }

    public void deleteUserByPhone(String phone) {
        log.debug("Deleting user by phone: {}", phone);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        UUID userId = user.getId();
        String email = user.getEmail();
        userRepository.delete(user);
        log.info("User deleted successfully with phone: {}", phone);
        
        // Отправляем событие в RabbitMQ
        userEventProducer.sendUserDeletedEvent(userId, email);
    }

    public Iterable<User> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll();
    }
}