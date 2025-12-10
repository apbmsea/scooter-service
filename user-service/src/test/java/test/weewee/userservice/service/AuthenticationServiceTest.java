package test.weewee.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.weewee.userservice.dto.*;
import test.weewee.userservice.exception.AuthException;
import test.weewee.userservice.exception.UserNotFoundException;
import test.weewee.userservice.model.User;
import test.weewee.userservice.repository.UserRepository;
import test.weewee.userservice.security.JwtUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Иван");
        testUser.setLastName("Петров");
        testUser.setPhone("+79123456789");
        testUser.setRole(User.Role.USER);
        // Устанавливаем даты для корректной работы mapToUserResponse
        testUser.setCreatedAt(java.time.LocalDateTime.now());
        testUser.setUpdatedAt(java.time.LocalDateTime.now());

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("Password123");
        registerRequest.setFirstName("Анна");
        registerRequest.setLastName("Смирнова");
        registerRequest.setPhone("+79001234567");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("signup: успешная регистрация нового пользователя")
    void signup_SuccessfulRegistration_ShouldReturnUser() {
        // Arrange
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(registerRequest.getPhone())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(testUserId);
            return user;
        });
        doNothing().when(userEventProducer).sendUserCreatedEvent(any(User.class));

        // Act
        User result = authenticationService.signup(registerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(registerRequest.getEmail(), result.getEmail());
        assertEquals(registerRequest.getFirstName(), result.getFirstName());
        assertEquals(registerRequest.getLastName(), result.getLastName());
        assertEquals(registerRequest.getPhone(), result.getPhone());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(userEventProducer).sendUserCreatedEvent(any(User.class));
    }

    @Test
    @DisplayName("signup: регистрация с существующим email - выбрасывает AuthException")
    void signup_ExistingEmail_ShouldThrowAuthException() {
        // Arrange
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.signup(registerRequest);
        });

        assertEquals("Пользователь с таким email уже существует", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userEventProducer, never()).sendUserCreatedEvent(any(User.class));
    }

    @Test
    @DisplayName("signup: регистрация с существующим телефоном - выбрасывает AuthException")
    void signup_ExistingPhone_ShouldThrowAuthException() {
        // Arrange
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(registerRequest.getPhone())).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.signup(registerRequest);
        });

        assertEquals("Пользователь с таким телефоном уже существует", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("authenticate: успешная аутентификация")
    void authenticate_SuccessfulLogin_ShouldReturnAuthResponse() {
        // Arrange
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        when(jwtUtil.generateAccessToken(testUserId, "USER")).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(testUserId)).thenReturn(refreshToken);

        // Act
        AuthResponse result = authenticationService.authenticate(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUser());
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
        assertEquals(testUser.getEmail(), result.getUser().getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateAccessToken(testUserId, "USER");
        verify(jwtUtil).generateRefreshToken(testUserId);
    }

    @Test
    @DisplayName("authenticate: пользователь не найден - выбрасывает AuthException")
    void authenticate_UserNotFound_ShouldThrowAuthException() {
        // Arrange
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Пользователь с таким email не найден", exception.getMessage());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtUtil, never()).generateAccessToken(any(), any());
    }

    @Test
    @DisplayName("authenticate: неверный пароль - выбрасывает AuthException")
    void authenticate_InvalidPassword_ShouldThrowAuthException() {
        // Arrange
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Неверный пароль", exception.getMessage());
        verify(jwtUtil, never()).generateAccessToken(any(), any());
    }

    @Test
    @DisplayName("refreshToken: успешное обновление токена")
    void refreshToken_ValidToken_ShouldReturnNewTokens() {
        // Arrange
        String oldRefreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        when(jwtUtil.validateToken(oldRefreshToken)).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(oldRefreshToken)).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateAccessToken(testUserId, "USER")).thenReturn(newAccessToken);
        when(jwtUtil.generateRefreshToken(testUserId)).thenReturn(newRefreshToken);

        // Act
        AuthResponse result = authenticationService.refreshToken(oldRefreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(newAccessToken, result.getAccessToken());
        assertEquals(newRefreshToken, result.getRefreshToken());
        assertNotNull(result.getUser());
        verify(jwtUtil).validateToken(oldRefreshToken);
        verify(jwtUtil).getUserIdFromToken(oldRefreshToken);
        verify(jwtUtil).generateAccessToken(testUserId, "USER");
        verify(jwtUtil).generateRefreshToken(testUserId);
    }

    @Test
    @DisplayName("refreshToken: null токен - выбрасывает AuthException")
    void refreshToken_NullToken_ShouldThrowAuthException() {
        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.refreshToken(null);
        });

        assertEquals("Refresh token отсутствует", exception.getMessage());
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    @DisplayName("refreshToken: невалидный токен - выбрасывает AuthException")
    void refreshToken_InvalidToken_ShouldThrowAuthException() {
        // Arrange
        String invalidToken = "invalidToken";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.refreshToken(invalidToken);
        });

        assertEquals("Недействительный refresh token", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("refreshToken: пользователь не найден - выбрасывает UserNotFoundException")
    void refreshToken_UserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        String validToken = "validToken";
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(validToken)).thenReturn(testUserId);
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authenticationService.refreshToken(validToken);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    @DisplayName("updatePassword: успешное обновление пароля")
    void updatePassword_SuccessfulUpdate_ShouldUpdatePassword() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("NewPassword123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        authenticationService.updatePassword(request);

        // Assert
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getNewPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("updatePassword: пользователь не найден - выбрасывает UserNotFoundException")
    void updatePassword_UserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("nonexistent@example.com");
        request.setNewPassword("NewPassword123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authenticationService.updatePassword(request);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("getCurrentUser: успешное получение пользователя")
    void getCurrentUser_UserExists_ShouldReturnUser() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act
        User result = authenticationService.getCurrentUser(email);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("getCurrentUser: пользователь не найден - выбрасывает UserNotFoundException")
    void getCurrentUser_UserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authenticationService.getCurrentUser(email);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    @DisplayName("updateUser: успешное обновление всех полей")
    void updateUser_UpdateAllFields_ShouldUpdateUser() {
        // Arrange
        String email = "test@example.com";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("newemail@example.com");
        request.setPhone("+79999999999");
        request.setFirstName("НовоеИмя");
        request.setLastName("НоваяФамилия");
        request.setPassword("NewPassword123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(userEventProducer).sendUserUpdatedEvent(any(User.class));

        // Act
        User result = authenticationService.updateUser(email, request);

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(request.getPassword());
        verify(userEventProducer).sendUserUpdatedEvent(any(User.class));
    }

    @Test
    @DisplayName("updateUser: обновление с существующим email - выбрасывает AuthException")
    void updateUser_ExistingEmail_ShouldThrowAuthException() {
        // Arrange
        String email = "test@example.com";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("existing@example.com");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.updateUser(email, request);
        });

        assertEquals("Пользователь с таким email уже существует", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("updateUser: обновление с существующим телефоном - выбрасывает AuthException")
    void updateUser_ExistingPhone_ShouldThrowAuthException() {
        // Arrange
        String email = "test@example.com";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPhone("+79999999999");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.updateUser(email, request);
        });

        assertEquals("Пользователь с таким телефоном уже существует", exception.getMessage());
    }

    @Test
    @DisplayName("updateUser: обновление без пароля - пароль не меняется")
    void updateUser_NoPassword_ShouldNotUpdatePassword() {
        // Arrange
        String email = "test@example.com";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("НовоеИмя");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(userEventProducer).sendUserUpdatedEvent(any(User.class));

        // Act
        authenticationService.updateUser(email, request);

        // Assert
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("logout: успешный выход")
    void logout_UserExists_ShouldCompleteSuccessfully() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act
        authenticationService.logout(email);

        // Assert
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("logout: пользователь не найден - завершается без ошибки")
    void logout_UserNotFound_ShouldCompleteWithoutError() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> {
            authenticationService.logout(email);
        });

        verify(userRepository).findByEmail(email);
    }
}

