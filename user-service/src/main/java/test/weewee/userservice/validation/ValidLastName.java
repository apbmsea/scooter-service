package test.weewee.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.LastName.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLastName {
    String message() default "Фамилия должна содержать только кириллицу, от 2 до 40 символов, начинаться с заглавной буквы. Разрешён дефис (например, Смирнов-Петров)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

