package test.weewee.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String PHONE_PATTERN = "^\\+[1-9]\\d{9,14}$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.trim().isEmpty()) {
            return false; //
        }

        String cleanPhone = phone.replaceAll("\\s+", "").replaceAll("-", "");

        boolean isValid = pattern.matcher(cleanPhone).matches();

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Неверный формат телефона. Пример: +79991234567"
            ).addConstraintViolation();
        }

        return isValid;
    }
}