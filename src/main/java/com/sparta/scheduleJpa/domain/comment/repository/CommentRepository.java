package com.sparta.scheduleJpa.domain.comment.repository;

import com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes" +
            "(c.id, c.user.name, c.content, c.createdAt, c.updatedAt) FROM Comment c " +
            "ORDER BY c.createdAt DESC")
    List<CommentReadDetailRes> findAllByScheduleIdOrderByCreatedAtDesc(Long scheduleId);
}
