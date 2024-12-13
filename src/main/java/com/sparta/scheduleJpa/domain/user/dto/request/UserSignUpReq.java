package com.sparta.scheduleJpa.domain.user.dto.request;

import com.sparta.scheduleJpa.domain.user.entity.User;

public record UserSignUpReq(
        String email,
        String name
) {
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }
}
