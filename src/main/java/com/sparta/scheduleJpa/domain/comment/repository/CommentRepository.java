package com.sparta.scheduleJpa.domain.comment.repository;

import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleIdOrderByCreatedAtDesc(Long scheduleId);
}
