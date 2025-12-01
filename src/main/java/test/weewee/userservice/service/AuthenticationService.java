package test.weewee.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.weewee.userservice.dto.*;
import test.weewee.userservice.exception.AuthException;
import test.weewee.userservice.exception.UserNotFoundException;
import test.weewee.userservice.model.User;
import test.weewee.userservice.repository.UserRepository;
import test.weewee.userservice.security.JwtUtil;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public User signup(RegisterRequest request) {
        log.info("Attempting to sign up user with email: {} and phone: {}", request.getEmail(), request.getPhone());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup failed - email already exists: {}", request.getEmail());
            throw new AuthException("Пользователь с таким email уже существует");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            log.warn("Signup failed - phone already exists: {}", request.getPhone());
            throw new AuthException("Пользователь с таким телефоном уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);
        log.info("User successfully registered with ID: {}", savedUser.getId());

        return savedUser;
    }

    public AuthResponse authenticate(LoginRequest request) {
        log.info("Authentication attempt for user: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Authentication failed - user not found: {}", request.getEmail());
                    return new AuthException("Пользователь с таким email не найден");
                });

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.debug("Spring Security authentication successful for: {}", request.getEmail());
        } catch (Exception e) {
            log.error("Spring Security authentication failed for: {}", request.getEmail(), e);
            throw new AuthException("Неверный пароль");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        log.info("Successful authentication for user: {}", request.getEmail());
        return AuthResponse.builder()
                .user(mapToUserResponse(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refresh token attempt");

        if (refreshToken == null) {
            log.warn("Refresh token attempt failed - token is null");
            throw new AuthException("Refresh token отсутствует");
        }

        refreshToken = cleanToken(refreshToken);

        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Refresh token validation failed");
            throw new AuthException("Недействительный refresh token");
        }

        UUID userId = jwtUtil.getUserIdFromToken(refreshToken);
        if (userId == null) {
            log.warn("Failed to extract user ID from refresh token");
            throw new AuthException("Неверный формат refresh token");
        }

        log.debug("Extracted user ID from refresh token: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Refresh token attempt failed - user not found by ID: {}", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });

        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        log.info("Refresh token successful for user: {}", user.getEmail());
        return AuthResponse.builder()
                .user(mapToUserResponse(user))
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String email) {
        log.info("Logout request for user: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            log.info("Successful logout for user: {}", email);
        } else {
            log.warn("Logout failed - user not found: {}", email);
        }
    }

    @Transactional
    public void updatePassword(ForgotPasswordRequest request) {
        log.info("Password reset attempt for: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Password reset failed - user not found: {}", request.getEmail());
                    return new UserNotFoundException("Пользователь не найден");
                });

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password successfully reset for: {}", request.getEmail());
    }

    public User getCurrentUser(String email) {
        log.debug("Getting current user: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", email);
                    return new UserNotFoundException("Пользователь не найден");
                });
    }

    @Transactional
    public User updateUser(String email, UpdateUserRequest request) {
        log.info("Update user attempt for: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Update user failed - user not found: {}", email);
                    return new UserNotFoundException("Пользователь не найден");
                });

        String oldEmail = user.getEmail();

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AuthException("Пользователь с таким email уже существует");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new AuthException("Пользователь с таким телефоном уже существует");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            log.debug("Updating password for user: {}", email);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            log.debug("Password not provided, keeping existing one for user: {}", email);
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        User updatedUser = userRepository.save(user);

        if (!oldEmail.equals(updatedUser.getEmail())) {
            log.info("User email changed: {} -> {}", oldEmail, updatedUser.getEmail());
        }

        log.info("User updated successfully: {}", updatedUser.getEmail());

        return updatedUser;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt().toString());
        response.setUpdatedAt(user.getUpdatedAt().toString());
        return response;
    }

    private String cleanToken(String token) {
        if (token == null) {
            return null;
        }

        String cleaned = token;
        cleaned = cleaned.replaceAll("[\"']", "");
        cleaned = cleaned.trim();
        cleaned = cleaned.replaceAll("(?i)bearer", "").trim();
        cleaned = cleaned.replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");
        return cleaned;
    }
}