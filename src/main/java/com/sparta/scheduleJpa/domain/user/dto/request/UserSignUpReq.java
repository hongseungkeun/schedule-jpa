package com.sparta.scheduleJpa.domain.user.dto.request;

import com.sparta.scheduleJpa.domain.user.entity.User;

public record UserSignUpReq(
        String email,
        String password,
        String name
) {

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .build();
    }
}
