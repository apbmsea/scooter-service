package test.weewee.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Пароль должен быть не менее 8 символов, содержать хотя бы одну букву и одну цифру, без пробелов";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}