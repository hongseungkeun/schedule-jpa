package com.sparta.scheduleJpa.domain.comment.repository;

import com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes" +
            "(c.id, c.content, c.createdAt, c.updatedAt, c.user.id, c.user.name) FROM Comment c " +
            "WHERE c.schedule.id = :schedule_id " +
            "ORDER BY c.createdAt DESC")
    List<CommentReadDetailRes> findAllByScheduleIdOrderByCreatedAtDesc(@Param("schedule_id") Long scheduleId);
}
