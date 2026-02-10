package ru.practicum.ewm.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class StringLengthValidation implements ConstraintValidator<StringLength, String> {
    private int min;
    private int max;

    @Override
    public void initialize(StringLength annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        return !(str.length() < min || str.length() > max);
    }
}
