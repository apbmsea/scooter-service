package test.weewee.userservice.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordValidator Tests")
class PasswordValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    @DisplayName("Валидный пароль: минимум 8 символов, есть буквы и цифры, нет пробелов")
    void valid_Password_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid("Password1", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("MyPass123", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("SecureP@ss1", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("12345678Ab", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль ровно 8 символов - валиден")
    void valid_PasswordExactly8Characters_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid("Pass1234", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("Abcdef12", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль меньше 8 символов - невалиден")
    void invalid_PasswordLessThan8Characters_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("Pass123", constraintValidatorContext)); // 7 символов
        assertFalse(passwordValidator.isValid("Pass1", constraintValidatorContext)); // 5 символов
        assertFalse(passwordValidator.isValid("P1", constraintValidatorContext)); // 2 символа
    }

    @Test
    @DisplayName("Пароль без цифр - невалиден")
    void invalid_PasswordWithoutDigits_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("Password", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("MySecurePass", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль без букв - невалиден")
    void invalid_PasswordWithoutLetters_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("12345678", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("9876543210", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль с пробелами - невалиден")
    void invalid_PasswordWithSpaces_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("Pass 1234", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("My Pass 123", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("Password1 ", constraintValidatorContext));
        assertFalse(passwordValidator.isValid(" Password1", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль только из букв - невалиден")
    void invalid_PasswordOnlyLetters_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("abcdefgh", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("ABCDEFGH", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("AbCdEfGh", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль только из цифр - невалиден")
    void invalid_PasswordOnlyDigits_ShouldReturnFalse() {
        assertFalse(passwordValidator.isValid("12345678", constraintValidatorContext));
        assertFalse(passwordValidator.isValid("9876543210", constraintValidatorContext));
    }

    @Test
    @DisplayName("Null значение - валидно (опциональное поле)")
    void valid_Null_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    @DisplayName("Пустая строка - валидна (опциональное поле)")
    void valid_EmptyString_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid("", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("   ", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль с спецсимволами (если есть буквы и цифры) - валиден")
    void valid_PasswordWithSpecialCharacters_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid("Pass@123", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("My#Pass1", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("Secure$Pass2", constraintValidatorContext));
    }

    @Test
    @DisplayName("Пароль с кириллицей (если есть буквы и цифры) - валиден")
    void valid_PasswordWithCyrillic_ShouldReturnTrue() {
        assertTrue(passwordValidator.isValid("Пароль123", constraintValidatorContext));
        assertTrue(passwordValidator.isValid("МойПароль1", constraintValidatorContext));
    }
}

