package com.sparta.scheduleJpa.domain.schedule.entity;

import com.sparta.scheduleJpa.domain.common.entity.AuditableEntity;
import com.sparta.scheduleJpa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Schedule(Long id, String title, String todo, User user) {
        this.id = id;
        this.title = title;
        this.todo = todo;
        this.user = user;
    }

    public void update(String title, String todo) {
        updateTitle(title);
        updateTodo(todo);
    }

    private void updateTitle(String title) {
        if (StringUtils.hasText(title)) {
            this.title = title;
        }
    }

    private void updateTodo(String todo) {
        if (StringUtils.hasText(todo)) {
            this.todo = todo;
        }
    }
}
