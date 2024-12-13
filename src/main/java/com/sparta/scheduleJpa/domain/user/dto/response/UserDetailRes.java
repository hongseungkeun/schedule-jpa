package com.sparta.scheduleJpa.domain.user.dto.response;

import com.sparta.scheduleJpa.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserDetailRes(
        Long id,
        String name,
        String email
) {
    public static UserDetailRes from(User user) {
        return UserDetailRes.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
