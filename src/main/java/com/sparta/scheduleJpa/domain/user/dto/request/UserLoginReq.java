package com.sparta.scheduleJpa.domain.user.dto.request;

public record UserLoginReq(
        String email,
        String password
) {
}
