package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.ValidationMessages;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryRequest {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private String name;
}
