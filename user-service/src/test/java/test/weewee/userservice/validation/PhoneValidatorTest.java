package test.weewee.userservice.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PhoneValidator Tests")
class PhoneValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    private PhoneValidator phoneValidator;

    @BeforeEach
    void setUp() {
        phoneValidator = new PhoneValidator();
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation())
                .thenReturn(constraintValidatorContext);
    }


    @Test
    @DisplayName("Валидный российский номер в формате +7XXXXXXXXXX")
    void valid_RussianPhone_ShouldReturnTrue() {
        assertTrue(phoneValidator.isValid("+79123456789", constraintValidatorContext));
        assertTrue(phoneValidator.isValid("+79001234567", constraintValidatorContext));
        assertTrue(phoneValidator.isValid("+79999999999", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с пробелами - должен очищаться и быть валидным")
    void valid_PhoneWithSpaces_ShouldReturnTrue() {
        assertTrue(phoneValidator.isValid("+7 912 345 67 89", constraintValidatorContext));
        assertTrue(phoneValidator.isValid("+7 900 123 45 67", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с дефисами - должен очищаться и быть валидным")
    void valid_PhoneWithHyphens_ShouldReturnTrue() {
        assertTrue(phoneValidator.isValid("+7-912-345-67-89", constraintValidatorContext));
        assertTrue(phoneValidator.isValid("+7-900-123-45-67", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с пробелами и дефисами - должен очищаться и быть валидным")
    void valid_PhoneWithSpacesAndHyphens_ShouldReturnTrue() {
        assertTrue(phoneValidator.isValid("+7 912-345-67-89", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер без +7 - невалиден")
    void invalid_PhoneWithoutPlus7_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("89123456789", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("79123456789", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("9123456789", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с другим кодом страны - невалиден")
    void invalid_PhoneWithOtherCountryCode_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("+19123456789", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("+44123456789", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("+38123456789", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с недостаточным количеством цифр - невалиден")
    void invalid_PhoneWithLessDigits_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("+7912345678", constraintValidatorContext)); // 9 цифр вместо 10
        assertFalse(phoneValidator.isValid("+791234567", constraintValidatorContext)); // 8 цифр
        assertFalse(phoneValidator.isValid("+7", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с избыточным количеством цифр - невалиден")
    void invalid_PhoneWithMoreDigits_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("+791234567890", constraintValidatorContext)); // 11 цифр вместо 10
        assertFalse(phoneValidator.isValid("+7912345678901", constraintValidatorContext)); // 12 цифр
    }

    @Test
    @DisplayName("Номер с буквами - невалиден")
    void invalid_PhoneWithLetters_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("+7abcdefghij", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("+7912345678a", constraintValidatorContext));
    }

    @Test
    @DisplayName("Null значение - невалидно")
    void invalid_Null_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    @DisplayName("Пустая строка - невалидна")
    void invalid_EmptyString_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("", constraintValidatorContext));
        assertFalse(phoneValidator.isValid("   ", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с 8 вместо +7 - невалиден")
    void invalid_PhoneStartingWith8_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("89123456789", constraintValidatorContext));
    }

    @Test
    @DisplayName("Номер с 7 без плюса - невалиден")
    void invalid_PhoneStartingWith7WithoutPlus_ShouldReturnFalse() {
        assertFalse(phoneValidator.isValid("79123456789", constraintValidatorContext));
    }
}

