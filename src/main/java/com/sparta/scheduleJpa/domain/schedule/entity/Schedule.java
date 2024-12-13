package com.sparta.scheduleJpa.domain.schedule.entity;

import com.sparta.scheduleJpa.domain.common.entity.AuditableEntity;
import com.sparta.scheduleJpa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Schedule(Long id, String title, String todo, User user) {
        this.id = id;
        this.title = title;
        this.todo = todo;
        this.user = user;
    }

    public void updateSchedule(String title, String todo) {
        updateTitle(title);
        updateTodo(todo);
    }

    private void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    private void updateTodo(String todo) {
        if (todo != null) {
            this.todo = todo;
        }
    }
}
