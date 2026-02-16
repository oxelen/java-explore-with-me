package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

import java.util.List;

import static ru.practicum.ewm.compilation.dto.StringLengthValues.TITLE_MAX_LENGTH;
import static ru.practicum.ewm.compilation.dto.StringLengthValues.TITLE_MIN_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    @UniqueElements(message = ValidationMessages.UNIQUE_ELEMENTS)
    private List<Long> events;

    private Boolean pinned = false;

    @StringLength(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
    private String title;
}
