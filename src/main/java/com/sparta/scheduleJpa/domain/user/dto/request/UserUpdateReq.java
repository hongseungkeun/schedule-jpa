package com.sparta.scheduleJpa.domain.user.dto.request;

import com.sparta.scheduleJpa.domain.user.dto.UserValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateReq(
        @Size(min = UserValidationMessages.PASSWORD_SIZE_MIN, message = UserValidationMessages.PASSWORD_SIZE_MIN_MESSAGE)
        @NotBlank(message = UserValidationMessages.PASSWORD_BLANK_MESSAGE)
        String password,

        @Size(max = UserValidationMessages.NAME_SIZE_MAX, message = UserValidationMessages.NAME_SIZE_MAX_MESSAGE)
        @NotBlank(message = UserValidationMessages.NAME_BLANK_MESSAGE)
        String name
) {
}
