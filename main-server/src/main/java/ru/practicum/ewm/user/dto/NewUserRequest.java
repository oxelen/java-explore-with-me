package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
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
public class NewUserRequest {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @Email(message = "wrong format")
    private String email;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    private String name;
}
