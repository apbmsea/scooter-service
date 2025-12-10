package test.weewee.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.FirstName.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFirstName {
    String message() default "Имя должно содержать только кириллицу, от 2 до 30 символов, начинаться с заглавной буквы. Разрешены дефисы для двойных имён (например, Анна-Мария)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

