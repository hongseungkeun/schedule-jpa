package com.sparta.scheduleJpa.domain.comment.service;

import com.sparta.scheduleJpa.domain.comment.dto.request.CommentCreateReq;
import com.sparta.scheduleJpa.domain.comment.dto.request.CommentUpdateReq;
import com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import com.sparta.scheduleJpa.domain.comment.exception.CommentNotFoundException;
import com.sparta.scheduleJpa.domain.comment.repository.CommentRepository;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.schedule.service.ScheduleService;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.service.UserService;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final ScheduleService scheduleService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long createComment(Long scheduleId, CommentCreateReq request, Long loginUserId) {
        User user = userService.findUserById(loginUserId);
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        Comment comment = commentRepository.save(request.toEntity(schedule, user));

        return comment.getId();
    }

    public List<CommentReadDetailRes> readOverallComments(Long scheduleId) {
        return commentRepository.findAllByScheduleIdOrderByCreatedAtDesc(scheduleId);
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateReq request, Long loginUserId) {
        Comment comment = checkUserAuthentication(commentId, loginUserId);

        comment.updateContent(request.content());
    }

    @Transactional
    public void deleteComment(Long commentId, Long loginUserId) {
        checkUserAuthentication(commentId, loginUserId);

        commentRepository.deleteById(commentId);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Comment checkUserAuthentication(Long commentId, Long loginUserId) {
        Comment comment = findCommentById(commentId);

        userService.checkUserAuthentication(comment.getUser().getId(), loginUserId);

        return comment;
    }
}
