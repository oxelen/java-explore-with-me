package ru.practicum.ewm.event.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.error.exception.ValidationException;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.util.validation.StringLengthValidation;

import static ru.practicum.ewm.util.validation.StringLengthValidation.*;

@Slf4j
public class NewEventValidation implements ConstraintValidator<ValidEvent, NewEventDto> {
    private static final int ANNOTATION_MIN_LENGTH = 20;
    private static final int ANNOTATION_MAX_LENGTH = 2000;
    private static final int DESCRIPTION_MIN_LENGTH = 20;
    private static final int DESCRIPTION_MAX_LENGTH = 7000;
    private static final int TITLE_MIN_LENGTH = 3;
    private static final int TITLE_MAX_LENGTH = 120;

    @Override
    public boolean isValid(NewEventDto event, ConstraintValidatorContext context) {
        try {
            isDescriptionInInterval(event.getDescription())
        }

        return false;
    }

    public boolean validate(NewEventDto event) {
        log.trace("start validation event");
        if (!isDescriptionInInterval(event.getDescription())) {

        }
        validateAnnotation(event.getAnnotation());
        validateTitle(event.getTitle());
        log.trace("event is valid");

        return true;
    }

    public boolean isDescriptionInInterval(String description) throws ValidationException {
        log.trace("description event validation");
        try {
            validateStringLength(description, DESCRIPTION_MIN_LENGTH, DESCRIPTION_MAX_LENGTH);
            log.trace("description is valid");
            return true;
        } catch (StringLengthValidation ex) {
            log.warn("description is not valid");
            throw new ValidationException("annotation " + ex.getMessage());
        }
    }

    public static void validateAnnotation(String annotation) {
        log.trace("annotation event validation");
        try {
            validateStringLength(annotation, ANNOTATION_MIN_LENGTH, ANNOTATION_MAX_LENGTH);
            log.trace("annotation is valid");
        } catch (StringLengthValidation ex) {
            log.warn("annotation is not valid");
            throw new ValidationException("annotation " + ex.getMessage());
        }
    }

    public static void validateTitle(String title) {
        log.trace("title event validation");
        try {
            validateStringLength(title, TITLE_MIN_LENGTH, TITLE_MAX_LENGTH);
            log.trace("title is valid");
        } catch (StringLengthValidation ex) {
            log.warn("title is not valid");
            throw new ValidationException("title " + ex.getMessage());
        }
    }
}
