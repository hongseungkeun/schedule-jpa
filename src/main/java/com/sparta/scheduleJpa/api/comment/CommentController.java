package com.sparta.scheduleJpa.api.comment;

import com.sparta.scheduleJpa.domain.comment.dto.request.CommentCreateReq;
import com.sparta.scheduleJpa.domain.comment.dto.request.CommentUpdateReq;
import com.sparta.scheduleJpa.domain.comment.dto.response.CommentReadDetailRes;
import com.sparta.scheduleJpa.domain.comment.service.CommentService;
import com.sparta.scheduleJpa.global.util.SessionUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @RequestBody @Valid final CommentCreateReq request,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        final Long commentId = commentService.createComment(request, loginUserId);

        final URI uri = UriComponentsBuilder.fromPath("/api/comments/{commentId}")
                .buildAndExpand(commentId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentReadDetailRes>> readComments(
            @RequestParam final Long scheduleId
    ) {
        return ResponseEntity.ok(commentService.readOverallComments(scheduleId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable final Long commentId,
            @RequestBody @Valid final CommentUpdateReq request,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        commentService.updateComment(commentId, request, loginUserId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable final Long commentId,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        commentService.deleteComment(commentId, loginUserId);

        return ResponseEntity.noContent().build();
    }
}
