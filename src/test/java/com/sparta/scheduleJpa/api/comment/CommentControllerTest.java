package com.sparta.scheduleJpa.api.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scheduleJpa.domain.comment.dto.request.CommentCreateReq;
import com.sparta.scheduleJpa.domain.comment.dto.request.CommentUpdateReq;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import com.sparta.scheduleJpa.domain.comment.repository.CommentRepository;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.schedule.repository.ScheduleRepository;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.repository.UserRepository;
import com.sparta.scheduleJpa.global.config.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class CommentControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserSignUpReq userRequest1 = new UserSignUpReq("abc@naver.com", "12345678", "테스트");
        UserSignUpReq userRequest2 = new UserSignUpReq("abc1@naver.com", "12345678", "테스트");
        userRepository.save(userRequest1.toEntity(passwordEncoder.encode(userRequest1.password())));
        userRepository.save(userRequest2.toEntity(passwordEncoder.encode(userRequest2.password())));

        User dummyUser1 = getUser(userRequest1.email());
        User dummyUser2 = getUser(userRequest2.email());

        List<ScheduleCreateReq> requestByUser1 = List.of(
                new ScheduleCreateReq("제목 테스트1", "1테스트입니다."),
                new ScheduleCreateReq("제목 테스트2", "2테스트입니다.")
        );

        List<ScheduleCreateReq> requestByUser2 = List.of(
                new ScheduleCreateReq("제목 테스트3", "3테스트입니다."),
                new ScheduleCreateReq("제목 테스트4", "4테스트입니다.")
        );

        for (ScheduleCreateReq req : requestByUser1) {
            scheduleRepository.save(req.toEntity(dummyUser1));
        }

        for (ScheduleCreateReq req : requestByUser2) {
            scheduleRepository.save(req.toEntity(dummyUser2));
        }

        List<CommentCreateReq> commentByUser1 = List.of(
                new CommentCreateReq("댓글 테스트1"),
                new CommentCreateReq("댓글 테스트2"),
                new CommentCreateReq("댓글 테스트3")

        );

        List<CommentCreateReq> commentByUser2 = List.of(
                new CommentCreateReq("댓글 테스트11"),
                new CommentCreateReq("댓글 테스트12"),
                new CommentCreateReq("댓글 테스트13")
        );

        Schedule schedule1 = scheduleRepository.findAll().stream().findFirst().get();
        Schedule schedule2 = scheduleRepository.findAll().stream().findAny().get();

        for (CommentCreateReq req : commentByUser1) {
            commentRepository.save(req.toEntity(schedule1, schedule1.getUser()));
        }

        for (CommentCreateReq req : commentByUser2) {
            commentRepository.save(req.toEntity(schedule2, schedule2.getUser()));
        }
    }

    @DisplayName("유저는 일정에 댓글을 추가할 수 있다.")
    @Test
    void create_comment() throws Exception {
        CommentCreateReq request = new CommentCreateReq("추가 댓글 생성");

        Schedule schedule = getSchedule();
        MockHttpSession dummySession = createDummySession(schedule.getUser().getId());

        mockMvc.perform(post("/api/schedules/{scheduleId}/comments", schedule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(dummySession))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("comment-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 식별자")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글")
                                        .attributes(Attributes.key("constraint").value("최대 100자"))
                        )
                ));
    }

    @DisplayName("일정의 댓글을 조회할 수 있다.")
    @Test
    void read_comments() throws Exception {
        Schedule schedule = getSchedule();

        mockMvc.perform(get("/api/schedules/{scheduleId}/comments", schedule.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-read-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 식별자")
                        ),
                        responseFields(
                                fieldWithPath("[].commentId").description("댓글 식별자"),
                                fieldWithPath("[].content").description("댓글"),
                                fieldWithPath("[].createdAt").description("댓글 작성일"),
                                fieldWithPath("[].updatedAt").description("댓글 수정일"),
                                fieldWithPath("[].userId").description("작성자 식별자"),
                                fieldWithPath("[].userName").description("작성자명")
                        )
                ));
    }

    @DisplayName("유저는 댓글을 수정할 수 있다.")
    @Test
    void update_comment() throws Exception {
        Comment comment = getComment();
        User dummyUser = comment.getUser();

        MockHttpSession dummySession = createDummySession(dummyUser.getId());

        CommentUpdateReq request = new CommentUpdateReq("댓글을 수정합니다.");

        mockMvc.perform(patch("/api/schedules/comments/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(dummySession))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글")
                                        .attributes(Attributes.key("constraint").value("최대 100자"))
                        )
                ));
    }

    @DisplayName("유저는 댓글을 삭제할 수 있다.")
    @Test
    void delete_comment() throws Exception {
        Comment comment = getComment();
        User dummyUser = comment.getUser();

        MockHttpSession dummySession = createDummySession(dummyUser.getId());

        mockMvc.perform(delete("/api/schedules/comments/{commentId}", comment.getId())
                        .session(dummySession))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("comment-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        )
                ));
    }

    private Comment getComment() {
        return commentRepository.findAll().stream().findFirst().get();
    }

    private Schedule getSchedule() {
        return scheduleRepository.findAll().stream().findFirst().get();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).get();
    }

    private MockHttpSession createDummySession(Long id) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("SESSION-KEY", id);
        return session;
    }
}
