package test.weewee.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NameValidator {

    private static final String NAME_PATTERN = "^[А-ЯЁ][А-Яа-яЁё]+(-[А-ЯЁ][А-Яа-яЁё]+)*$";
    private static final Pattern pattern = Pattern.compile(NAME_PATTERN);

    private static boolean validateName(String name, int minLength, int maxLength) {
        if (name == null) {
            return false;
        }

        if (name.length() < minLength || name.length() > maxLength) {
            return false;
        }

        return pattern.matcher(name).matches();
    }

    public static class FirstName implements ConstraintValidator<ValidFirstName, String> {
        @Override
        public boolean isValid(String firstName, ConstraintValidatorContext context) {
            return validateName(firstName, 2, 30);
        }
    }

    public static class LastName implements ConstraintValidator<ValidLastName, String> {
        @Override
        public boolean isValid(String lastName, ConstraintValidatorContext context) {
            return validateName(lastName, 2, 40);
        }
    }
}
