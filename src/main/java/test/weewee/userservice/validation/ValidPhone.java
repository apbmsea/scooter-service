package test.weewee.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Неверный формат телефона. Используйте международный формат: +79991234567";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}