package test.weewee.userservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET = "testSecretKeyForJwtUtilTestingPurposesOnlyMustBeLongEnough";
    private static final long TEST_EXPIRATION = 3600000; // 1 час
    private static final long TEST_REFRESH_EXPIRATION = 86400000; // 24 часа

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", TEST_REFRESH_EXPIRATION);
    }

    @Test
    @DisplayName("Генерация access токена: успешно создается токен")
    void generateAccessToken_ShouldCreateValidToken() {
        UUID userId = UUID.randomUUID();
        String role = "USER";

        String token = jwtUtil.generateAccessToken(userId, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50); // JWT токены обычно длинные
    }

    @Test
    @DisplayName("Генерация access токена: токен содержит userId и role")
    void generateAccessToken_ShouldContainUserIdAndRole() {
        UUID userId = UUID.randomUUID();
        String role = "ADMIN";

        String token = jwtUtil.generateAccessToken(userId, role);

        UUID extractedUserId = jwtUtil.getUserIdFromToken(token);
        assertEquals(userId, extractedUserId);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Генерация refresh токена: успешно создается токен")
    void generateRefreshToken_ShouldCreateValidToken() {
        UUID userId = UUID.randomUUID();

        String token = jwtUtil.generateRefreshToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50);
    }

    @Test
    @DisplayName("Генерация refresh токена: токен содержит userId")
    void generateRefreshToken_ShouldContainUserId() {
        UUID userId = UUID.randomUUID();

        String token = jwtUtil.generateRefreshToken(userId);

        UUID extractedUserId = jwtUtil.getUserIdFromToken(token);
        assertEquals(userId, extractedUserId);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Валидация токена: валидный токен возвращает true")
    void validateToken_ValidToken_ShouldReturnTrue() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "USER");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Валидация токена: невалидный токен возвращает false")
    void validateToken_InvalidToken_ShouldReturnFalse() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Валидация токена: null токен возвращает false")
    void validateToken_NullToken_ShouldReturnFalse() {
        boolean isValid = jwtUtil.validateToken(null);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Валидация токена: пустая строка возвращает false")
    void validateToken_EmptyToken_ShouldReturnFalse() {
        boolean isValid = jwtUtil.validateToken("");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Извлечение userId: успешно извлекается из access токена")
    void getUserIdFromToken_ValidAccessToken_ShouldReturnUserId() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "USER");

        UUID extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Извлечение userId: успешно извлекается из refresh токена")
    void getUserIdFromToken_ValidRefreshToken_ShouldReturnUserId() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateRefreshToken(userId);

        UUID extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Извлечение userId: невалидный токен возвращает null")
    void getUserIdFromToken_InvalidToken_ShouldReturnNull() {
        String invalidToken = "invalid.token.here";

        UUID extractedUserId = jwtUtil.getUserIdFromToken(invalidToken);

        assertNull(extractedUserId);
    }

    @Test
    @DisplayName("Извлечение userId: null токен возвращает null")
    void getUserIdFromToken_NullToken_ShouldReturnNull() {
        UUID extractedUserId = jwtUtil.getUserIdFromToken(null);

        assertNull(extractedUserId);
    }

    @Test
    @DisplayName("Разные роли: токены генерируются для разных ролей")
    void generateAccessToken_DifferentRoles_ShouldGenerateDifferentTokens() {
        UUID userId = UUID.randomUUID();
        String tokenUser = jwtUtil.generateAccessToken(userId, "USER");
        String tokenAdmin = jwtUtil.generateAccessToken(userId, "ADMIN");

        assertNotEquals(tokenUser, tokenAdmin);
        assertTrue(jwtUtil.validateToken(tokenUser));
        assertTrue(jwtUtil.validateToken(tokenAdmin));
    }

    @Test
    @DisplayName("Разные пользователи: токены генерируются для разных пользователей")
    void generateAccessToken_DifferentUsers_ShouldGenerateDifferentTokens() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        String token1 = jwtUtil.generateAccessToken(userId1, "USER");
        String token2 = jwtUtil.generateAccessToken(userId2, "USER");

        assertNotEquals(token1, token2);
        assertEquals(userId1, jwtUtil.getUserIdFromToken(token1));
        assertEquals(userId2, jwtUtil.getUserIdFromToken(token2));
    }

    @Test
    @DisplayName("Access и refresh токены: разные типы токенов для одного пользователя")
    void generateTokens_DifferentTypes_ShouldGenerateDifferentTokens() {
        UUID userId = UUID.randomUUID();
        String accessToken = jwtUtil.generateAccessToken(userId, "USER");
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        assertNotEquals(accessToken, refreshToken);
        assertEquals(userId, jwtUtil.getUserIdFromToken(accessToken));
        assertEquals(userId, jwtUtil.getUserIdFromToken(refreshToken));
    }
}

