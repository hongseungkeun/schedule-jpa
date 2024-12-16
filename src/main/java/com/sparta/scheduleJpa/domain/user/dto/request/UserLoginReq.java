package com.sparta.scheduleJpa.domain.user.dto.request;

import com.sparta.scheduleJpa.domain.user.dto.UserValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginReq(
        @Pattern(regexp = UserValidationMessages.EMAIL_PATTERN_REG, message = UserValidationMessages.EMAIL_PATTERN_MESSAGE)
        @NotBlank(message = UserValidationMessages.EMAIL_BLANK_MESSAGE)
        String email,

        @Size(min = UserValidationMessages.PASSWORD_SIZE_MIN, message = UserValidationMessages.PASSWORD_SIZE_MIN_MESSAGE)
        @NotBlank(message = UserValidationMessages.PASSWORD_BLANK_MESSAGE)
        String password
) {
}
