package com.sparta.scheduleJpa.domain.comment.entity;

import com.sparta.scheduleJpa.domain.common.entity.AuditableEntity;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
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
public class Comment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Comment(Long id, String content, Schedule schedule, User user) {
        this.id = id;
        this.content = content;
        this.schedule = schedule;
        this.user = user;
    }

    public void updateContent(String content) {
        if (StringUtils.hasText(content)) {
            this.content = content;
        }
    }
}
