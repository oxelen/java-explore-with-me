package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.dto.validation.values.UserStringLengthValues;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @Email(message = "wrong format")
    @StringLength(min = UserStringLengthValues.EMAIL_MIN_LENGTH, max = UserStringLengthValues.EMAIL_MAX_LENGTH)
    private String email;

    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = UserStringLengthValues.NAME_MIN_LENGTH, max = UserStringLengthValues.NAME_MAX_LENGTH)
    private String name;
}
