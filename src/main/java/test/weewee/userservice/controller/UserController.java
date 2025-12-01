package test.weewee.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import test.weewee.userservice.dto.ErrorResponse;
import test.weewee.userservice.dto.UpdateUserRequest;
import test.weewee.userservice.dto.UserResponse;
import test.weewee.userservice.exception.AuthException;
import test.weewee.userservice.exception.UserNotFoundException;
import test.weewee.userservice.model.User;
import test.weewee.userservice.security.JwtUtil;
import test.weewee.userservice.service.AuthenticationService;
import test.weewee.userservice.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpServletRequest request) {
        log.debug("GET /users/me - get current user");

        String userEmail = extractUserEmailFromRequest(request);
        if (userEmail == null) {
            log.warn("Unauthorized access to /users/me");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            User user = authenticationService.getCurrentUser(userEmail);
            UserResponse userResponse = mapToUserResponse(user);
            log.debug("User found: {}", user.getEmail());
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.warn("User not found: {}", userEmail);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            HttpServletRequest request,
            @Valid @RequestBody UpdateUserRequest updateRequest) {
        log.debug("PUT /users/me - update current user");

        String userEmail = extractUserEmailFromRequest(request);
        if (userEmail == null) {
            log.warn("Unauthorized attempt to update user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            User updatedUser = authenticationService.updateUser(userEmail, updateRequest);
            UserResponse userResponse = mapToUserResponse(updatedUser);

            log.info("User updated successfully: {}", updatedUser.getEmail());
            return ResponseEntity.ok(userResponse);
        } catch (AuthException e) {
            log.error("Update user failed - auth error: {}", userEmail, e);
            if (e.getMessage().contains("email")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.of("Ошибка обновления пользователя", Map.of("email", e.getMessage())));
            } else if (e.getMessage().contains("телефон")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.of("Ошибка обновления пользователя", Map.of("phone", e.getMessage())));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.of("Ошибка обновления пользователя", "error", e.getMessage()));
            }
        } catch (Exception e) {
            log.error("Update user failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of("Ошибка обновления пользователя", "error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        log.debug("GET /users - get all users");

        if (!isAdmin(request)) {
            log.warn("Unauthorized attempt to get all users");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of("Доступ запрещен", "error", "Только администраторы могут просматривать список пользователей"));
        }

        try {
            Iterable<User> users = userService.getAllUsers();
            List<UserResponse> userResponses = StreamSupport.stream(users.spliterator(), false)
                    .map(this::mapToUserResponse)
                    .collect(Collectors.toList());
            log.info("Retrieved {} users", userResponses.size());
            return ResponseEntity.ok(userResponses);
        } catch (Exception e) {
            log.error("Failed to get all users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("Ошибка получения списка пользователей", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(
            HttpServletRequest request,
            @PathVariable String id) {
        log.debug("DELETE /users/{} - delete user by ID", id);

        if (!isAdmin(request)) {
            log.warn("Unauthorized attempt to delete user by ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of("Доступ запрещен", "error", "Только администраторы могут удалять пользователей"));
        }

        try {
            UUID userId = UUID.fromString(id);
            userService.deleteUser(userId);
            log.info("User deleted successfully with ID: {}", id);
            return ResponseEntity.ok(Map.of("message", "Пользователь успешно удален"));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of("Неверный формат ID", "error", "ID должен быть в формате UUID"));
        } catch (UserNotFoundException e) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of("Пользователь не найден", "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to delete user by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("Ошибка удаления пользователя", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/by-email/{email}")
    public ResponseEntity<?> deleteUserByEmail(
            HttpServletRequest request,
            @PathVariable String email) {
        log.debug("DELETE /users/by-email/{} - delete user by email", email);

        if (!isAdmin(request)) {
            log.warn("Unauthorized attempt to delete user by email: {}", email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of("Доступ запрещен", "error", "Только администраторы могут удалять пользователей"));
        }

        try {
            userService.deleteUserByEmail(email);
            log.info("User deleted successfully with email: {}", email);
            return ResponseEntity.ok(Map.of("message", "Пользователь успешно удален"));
        } catch (UserNotFoundException e) {
            log.warn("User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of("Пользователь не найден", "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to delete user by email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("Ошибка удаления пользователя", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/by-phone/{phone}")
    public ResponseEntity<?> deleteUserByPhone(
            HttpServletRequest request,
            @PathVariable String phone) {
        log.debug("DELETE /users/by-phone/{} - delete user by phone", phone);

        if (!isAdmin(request)) {
            log.warn("Unauthorized attempt to delete user by phone: {}", phone);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of("Доступ запрещен", "error", "Только администраторы могут удалять пользователей"));
        }

        try {
            userService.deleteUserByPhone(phone);
            log.info("User deleted successfully with phone: {}", phone);
            return ResponseEntity.ok(Map.of("message", "Пользователь успешно удален"));
        } catch (UserNotFoundException e) {
            log.warn("User not found with phone: {}", phone);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of("Пользователь не найден", "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to delete user by phone: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("Ошибка удаления пользователя", "error", e.getMessage()));
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        String userEmail = extractUserEmailFromRequest(request);
        if (userEmail == null) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
    }

    private String extractUserEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            token = cleanToken(token);
            if (jwtUtil.validateToken(token)) {
                return jwtUtil.getEmailFromToken(token);
            }
        }
        return null;
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
}