package com.sparta.scheduleJpa.domain.user.entity;

import com.sparta.scheduleJpa.domain.common.entity.BaseEntity;
import com.sparta.scheduleJpa.global.exception.UnauthorizedException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void checkLoginUser(Long loginUserId) {
        if (!this.getId().equals(loginUserId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

    public void update(String name, String password) {
        updateName(name);
        updatePassword(password);
    }

    private void updateName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
    }

    private void updatePassword(String password) {
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
    }
}
