package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.validation.values.CategoryStringLengthValues;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryRequest {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = CategoryStringLengthValues.NAME_MIN_LENGTH, max = CategoryStringLengthValues.NAME_MAX_LENGTH)
    private String name;
}
