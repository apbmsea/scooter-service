package test.weewee.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.weewee.userservice.exception.UserNotFoundException;
import test.weewee.userservice.model.User;
import test.weewee.userservice.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Иван");
        testUser.setLastName("Петров");
        testUser.setPhone("+79123456789");
        testUser.setRole(User.Role.USER);
    }

    @Test
    @DisplayName("createUser: успешное создание пользователя")
    void createUser_SuccessfulCreation_ShouldReturnSavedUser() {
        // Arrange
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(testUserId);
            return user;
        });

        // Act
        User result = userService.createUser(testUser);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("findByEmail: пользователь найден")
    void findByEmail_UserExists_ShouldReturnUser() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("findByEmail: пользователь не найден")
    void findByEmail_UserNotFound_ShouldReturnEmpty() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("findById: пользователь найден")
    void findById_UserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findById(testUserId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(testUserId);
    }

    @Test
    @DisplayName("findById: пользователь не найден")
    void findById_UserNotFound_ShouldReturnEmpty() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("existsByEmail: email существует")
    void existsByEmail_EmailExists_ShouldReturnTrue() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userService.existsByEmail(email);

        // Assert
        assertTrue(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("existsByEmail: email не существует")
    void existsByEmail_EmailNotExists_ShouldReturnFalse() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userService.existsByEmail(email);

        // Assert
        assertFalse(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("findByPhone: пользователь найден")
    void findByPhone_UserExists_ShouldReturnUser() {
        // Arrange
        String phone = "+79123456789";
        when(userRepository.findByPhone(phone)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByPhone(phone);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByPhone(phone);
    }

    @Test
    @DisplayName("findByPhone: пользователь не найден")
    void findByPhone_UserNotFound_ShouldReturnEmpty() {
        // Arrange
        String phone = "+79999999999";
        when(userRepository.findByPhone(phone)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByPhone(phone);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findByPhone(phone);
    }

    @Test
    @DisplayName("deleteUser: успешное удаление по ID")
    void deleteUser_ById_SuccessfulDeletion() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).deleteById(testUserId);
        doNothing().when(userEventProducer).sendUserDeletedEvent(testUserId, testUser.getEmail());

        // Act
        userService.deleteUser(testUserId);

        // Assert
        verify(userRepository).findById(testUserId);
        verify(userRepository).deleteById(testUserId);
        verify(userEventProducer).sendUserDeletedEvent(testUserId, testUser.getEmail());
    }

    @Test
    @DisplayName("deleteUser: пользователь не найден - выбрасывает UserNotFoundException")
    void deleteUser_ById_UserNotFound_ShouldThrowException() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(nonExistentId);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
        verify(userEventProducer, never()).sendUserDeletedEvent(any(), any());
    }

    @Test
    @DisplayName("deleteUserByEmail: успешное удаление по email")
    void deleteUserByEmail_SuccessfulDeletion() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);
        doNothing().when(userEventProducer).sendUserDeletedEvent(testUserId, email);

        // Act
        userService.deleteUserByEmail(email);

        // Assert
        verify(userRepository).findByEmail(email);
        verify(userRepository).delete(testUser);
        verify(userEventProducer).sendUserDeletedEvent(testUserId, email);
    }

    @Test
    @DisplayName("deleteUserByEmail: пользователь не найден - выбрасывает UserNotFoundException")
    void deleteUserByEmail_UserNotFound_ShouldThrowException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserByEmail(email);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteUserByPhone: успешное удаление по телефону")
    void deleteUserByPhone_SuccessfulDeletion() {
        // Arrange
        String phone = "+79123456789";
        when(userRepository.findByPhone(phone)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);
        doNothing().when(userEventProducer).sendUserDeletedEvent(testUserId, testUser.getEmail());

        // Act
        userService.deleteUserByPhone(phone);

        // Assert
        verify(userRepository).findByPhone(phone);
        verify(userRepository).delete(testUser);
        verify(userEventProducer).sendUserDeletedEvent(testUserId, testUser.getEmail());
    }

    @Test
    @DisplayName("deleteUserByPhone: пользователь не найден - выбрасывает UserNotFoundException")
    void deleteUserByPhone_UserNotFound_ShouldThrowException() {
        // Arrange
        String phone = "+79999999999";
        when(userRepository.findByPhone(phone)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserByPhone(phone);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("getAllUsers: успешное получение всех пользователей")
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        Iterable<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        int count = 0;
        for (@SuppressWarnings("unused") User user : result) {
            count++;
        }
        assertEquals(2, count);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("getAllUsers: пустой список пользователей")
    void getAllUsers_EmptyList_ShouldReturnEmpty() {
        // Arrange
        List<User> emptyList = List.of();
        when(userRepository.findAll()).thenReturn(emptyList);

        // Act
        Iterable<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        int count = 0;
        for (@SuppressWarnings("unused") User user : result) {
            count++;
        }
        assertEquals(0, count);
        verify(userRepository).findAll();
    }
}

