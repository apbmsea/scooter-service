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
@DisplayName("NameValidator Tests")
class NameValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private NameValidator.FirstName firstNameValidator;
    private NameValidator.LastName lastNameValidator;

    @BeforeEach
    void setUp() {
        firstNameValidator = new NameValidator.FirstName();
        lastNameValidator = new NameValidator.LastName();
    }

    @Test
    @DisplayName("FirstName: валидное имя с заглавной буквы")
    void firstName_ValidName_ShouldReturnTrue() {
        assertTrue(firstNameValidator.isValid("Иван", constraintValidatorContext));
        assertTrue(firstNameValidator.isValid("Анна", constraintValidatorContext));
        assertTrue(firstNameValidator.isValid("Мария", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: валидное двойное имя с дефисом")
    void firstName_ValidDoubleName_ShouldReturnTrue() {
        assertTrue(firstNameValidator.isValid("Анна-Мария", constraintValidatorContext));
        assertTrue(firstNameValidator.isValid("Мария-Анна", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: имя с маленькой буквы - невалидно")
    void firstName_LowercaseFirstLetter_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid("иван", constraintValidatorContext));
        assertFalse(firstNameValidator.isValid("анна", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: слишком короткое имя (меньше 2 символов)")
    void firstName_TooShort_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid("А", constraintValidatorContext));
        assertFalse(firstNameValidator.isValid("Я", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: слишком длинное имя (больше 30 символов)")
    void firstName_TooLong_ShouldReturnFalse() {
        String longName = "А".repeat(31);
        assertFalse(firstNameValidator.isValid(longName, constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: имя ровно 2 символа - валидно")
    void firstName_ExactlyTwoCharacters_ShouldReturnTrue() {
        assertTrue(firstNameValidator.isValid("Ан", constraintValidatorContext));
        assertTrue(firstNameValidator.isValid("Ян", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: имя ровно 30 символов - валидно")
    void firstName_ExactlyThirtyCharacters_ShouldReturnTrue() {
        String name = "А".repeat(30);
        assertTrue(firstNameValidator.isValid(name, constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: null значение - невалидно")
    void firstName_Null_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: содержит латиницу - невалидно")
    void firstName_ContainsLatin_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid("ИванIvan", constraintValidatorContext));
        assertFalse(firstNameValidator.isValid("Anna", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: содержит цифры - невалидно")
    void firstName_ContainsDigits_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid("Иван123", constraintValidatorContext));
    }

    @Test
    @DisplayName("FirstName: двойное имя с маленькой буквы после дефиса - невалидно")
    void firstName_DoubleNameWithLowercaseAfterHyphen_ShouldReturnFalse() {
        assertFalse(firstNameValidator.isValid("Анна-мария", constraintValidatorContext));
        assertFalse(firstNameValidator.isValid("анна-Мария", constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: валидная фамилия с заглавной буквы")
    void lastName_ValidLastName_ShouldReturnTrue() {
        assertTrue(lastNameValidator.isValid("Петров", constraintValidatorContext));
        assertTrue(lastNameValidator.isValid("Смирнов", constraintValidatorContext));
        assertTrue(lastNameValidator.isValid("Иванов", constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: валидная двойная фамилия с дефисом")
    void lastName_ValidDoubleLastName_ShouldReturnTrue() {
        assertTrue(lastNameValidator.isValid("Смирнов-Петров", constraintValidatorContext));
        assertTrue(lastNameValidator.isValid("Иванов-Сидоров", constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: слишком короткая фамилия (меньше 2 символов)")
    void lastName_TooShort_ShouldReturnFalse() {
        assertFalse(lastNameValidator.isValid("П", constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: слишком длинная фамилия (больше 40 символов)")
    void lastName_TooLong_ShouldReturnFalse() {
        String longLastName = "П".repeat(41);
        assertFalse(lastNameValidator.isValid(longLastName, constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: фамилия ровно 2 символа - валидна")
    void lastName_ExactlyTwoCharacters_ShouldReturnTrue() {
        assertTrue(lastNameValidator.isValid("Пе", constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: фамилия ровно 40 символов - валидна")
    void lastName_ExactlyFortyCharacters_ShouldReturnTrue() {
        String lastName = "П".repeat(40);
        assertTrue(lastNameValidator.isValid(lastName, constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: null значение - невалидно")
    void lastName_Null_ShouldReturnFalse() {
        assertFalse(lastNameValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    @DisplayName("LastName: содержит латиницу - невалидно")
    void lastName_ContainsLatin_ShouldReturnFalse() {
        assertFalse(lastNameValidator.isValid("ПетровPetrov", constraintValidatorContext));
    }
}

