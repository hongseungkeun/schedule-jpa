package com.sparta.scheduleJpa.domain.user.entity;

import com.sparta.scheduleJpa.domain.common.entity.BaseEntity;
import com.sparta.scheduleJpa.domain.user.exception.LoginFailedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void isPossibleLogin(String password) {
        if (!this.password.equals(password)) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }
    }
}
