package ru.practicum.ewm.comments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comments.dto.validation.values.CommentStringLengthValues;
import ru.practicum.ewm.util.ValidationMessages;
import ru.practicum.ewm.validation.StringLength;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank(message = ValidationMessages.NOT_BLANK)
    @StringLength(min = CommentStringLengthValues.TEXT_MIN_LENGTH, max = CommentStringLengthValues.TEXT_MAX_LENGTH)
    private String text;
}
