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
@DisplayName("EmailValidator Tests")
class EmailValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    @DisplayName("Валидный email: стандартный формат")
    void valid_StandardEmail_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test@domain.ru", constraintValidatorContext));
        assertTrue(emailValidator.isValid("admin@company.org", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с поддоменом")
    void valid_EmailWithSubdomain_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user@mail.example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test@sub.domain.ru", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с цифрами")
    void valid_EmailWithDigits_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user123@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test@example123.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с точками в локальной части")
    void valid_EmailWithDotsInLocalPart_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user.name@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("first.last@domain.ru", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с плюсом в локальной части")
    void valid_EmailWithPlus_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user+tag@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test+123@domain.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с дефисом")
    void valid_EmailWithHyphen_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user-name@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test@my-domain.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Валидный email: с подчеркиванием")
    void valid_EmailWithUnderscore_ShouldReturnTrue() {
        assertTrue(emailValidator.isValid("user_name@example.com", constraintValidatorContext));
        assertTrue(emailValidator.isValid("test_user@domain.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: без @")
    void invalid_EmailWithoutAt_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("userexample.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("testdomain.ru", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: без домена")
    void invalid_EmailWithoutDomain_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("user@", constraintValidatorContext));
        assertFalse(emailValidator.isValid("test@.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: без локальной части")
    void invalid_EmailWithoutLocalPart_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("@example.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("@domain.ru", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: без TLD")
    void invalid_EmailWithoutTLD_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("user@example", constraintValidatorContext));
        assertFalse(emailValidator.isValid("test@domain", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: с пробелами")
    void invalid_EmailWithSpaces_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("user @example.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user@example .com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("user name@example.com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: с несколькими @")
    void invalid_EmailWithMultipleAt_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("user@@example.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("test@domain@com", constraintValidatorContext));
    }

    @Test
    @DisplayName("Невалидный email: TLD меньше 2 символов")
    void invalid_EmailWithShortTLD_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("user@example.c", constraintValidatorContext));
    }

    @Test
    @DisplayName("Null значение - невалидно")
    void invalid_Null_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    @DisplayName("Пустая строка - невалидна")
    void invalid_EmptyString_ShouldReturnFalse() {
        assertFalse(emailValidator.isValid("", constraintValidatorContext));
    }
}

