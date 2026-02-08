package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryRequest {
    @NotBlank(message = "must not be blank")
    private String name;
}
