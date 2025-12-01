package test.weewee.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            return true;
        }

        if (password.trim().isEmpty()) {
            return true;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean noSpaces = !password.contains(" ");

        return hasDigit && hasLetter && noSpaces;
    }
}