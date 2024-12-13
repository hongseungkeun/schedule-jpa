package com.sparta.scheduleJpa.domain.schedule.entity;

import com.sparta.scheduleJpa.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String todo;

    @Builder
    public Schedule(Long id, String userName, String title, String todo) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.todo = todo;
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
