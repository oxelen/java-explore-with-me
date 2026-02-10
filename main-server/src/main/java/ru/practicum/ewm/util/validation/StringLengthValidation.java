package ru.practicum.ewm.util.validation;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.error.exception.ValidationException;

public class StringLengthValidation extends Throwable {
    public static void validateStringLength(String str, int min, int max) throws StringLengthValidation {
        if (str.length() < min || str.length() > max) {
            throw new ValidationException("length must be more than " + min +
                    " and less than " + max);
        }
    }
}
