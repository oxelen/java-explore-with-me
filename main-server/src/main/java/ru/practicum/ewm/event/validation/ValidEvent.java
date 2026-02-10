package ru.practicum.ewm.event.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NewEventValidation.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEvent {
    String message() default "Event is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
