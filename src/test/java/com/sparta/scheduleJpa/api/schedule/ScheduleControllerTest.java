package com.sparta.scheduleJpa.api.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleUpdateReq;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class ScheduleControllerTest {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
                new ScheduleCreateReq("제목 테스트2", "2테스트입니다."),
                new ScheduleCreateReq("제목 테스트3", "3테스트입니다."),
                new ScheduleCreateReq("제목 테스트4", "4테스트입니다."),
                new ScheduleCreateReq("제목 테스트5", "5테스트입니다.")
        );

        List<ScheduleCreateReq> requestByUser2 = List.of(
                new ScheduleCreateReq("제목 테스트6", "6테스트입니다."),
                new ScheduleCreateReq("제목 테스트7", "7테스트입니다."),
                new ScheduleCreateReq("제목 테스트8", "8테스트입니다."),
                new ScheduleCreateReq("제목 테스트9", "9테스트입니다."),
                new ScheduleCreateReq("제목 테스트10", "10테스트입니다.")
        );

        for (ScheduleCreateReq req : requestByUser1) {
            scheduleRepository.save(req.toEntity(dummyUser1));
        }

        for (ScheduleCreateReq req : requestByUser2) {
            scheduleRepository.save(req.toEntity(dummyUser2));
        }
    }

    @DisplayName("유저는 일정을 추가할 수 있다.")
    @Test
    void create_schedule() throws Exception {
        ScheduleCreateReq request = new ScheduleCreateReq("제목 테스트11", "11테스트입니다.");

        MockHttpSession dummySession = createDummySession(getUserId());

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(dummySession))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("schedule-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("일정 제목")
                                        .attributes(Attributes.key("constraint").value("최대 10자")),
                                fieldWithPath("todo").description("일정 할일")
                                        .attributes(Attributes.key("constraint").value("최대 200자"))
                        )
                ));
    }

    @DisplayName("일정을 전체 페이징 조회할 수 있다.")
    @Test
    void read_schedules() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "3");

        mockMvc.perform(get("/api/schedules")
                        .queryParams(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("schedule-read-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].scheduleId").description("일정 식별자"),
                                fieldWithPath("content[].title").description("일정 제목"),
                                fieldWithPath("content[].todo").description("일정 할일"),
                                fieldWithPath("content[].createdAt").description("일정 작성일"),
                                fieldWithPath("content[].updatedAt").description("일정 수정일"),
                                fieldWithPath("content[].userId").description("작성자 식별자"),
                                fieldWithPath("content[].userName").description("작성자명"),
                                fieldWithPath("content[].commentCount").description("댓글 개수"),
                                fieldWithPath("pageable").description("페이징 관련 정보"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소 수"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("sort.empty").description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 여부"),
                                fieldWithPath("first").description("첫 번째 페이지 여부"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 개수"),
                                fieldWithPath("empty").description("페이지가 비어있는지 여부")
                        )
                ));
    }

    @DisplayName("일정을 단건 조회할 수 있다.")
    @Test
    void read_schedule() throws Exception {
        Long scheduleId = getSchedule().getId();

        mockMvc.perform(get("/api/schedules/{scheduleId}", scheduleId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("schedule-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("조회할 일정 번호")
                        ),
                        responseFields(
                                fieldWithPath("scheduleId").description("일정 식별자"),
                                fieldWithPath("title").description("일정 제목"),
                                fieldWithPath("todo").description("일정 할일"),
                                fieldWithPath("createdAt").description("일정 작성일"),
                                fieldWithPath("updatedAt").description("일정 수정일"),
                                fieldWithPath("userId").description("작성자 식별자"),
                                fieldWithPath("userName").description("작성자명"),
                                fieldWithPath("commentCount").description("댓글 개수")
                        )
                ));
    }

    @DisplayName("유저는 일정을 수정할 수 있다.")
    @Test
    void update_schedule() throws Exception {
        Schedule schedule = getSchedule();
        User dummyUser = schedule.getUser();

        MockHttpSession dummySession = createDummySession(dummyUser.getId());

        ScheduleUpdateReq request = new ScheduleUpdateReq("테스트 변경", "테스트가 변경되었습니다");

        mockMvc.perform(patch("/api/schedules/{scheduleId}", schedule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(dummySession))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("schedule-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("조회할 일정 번호")
                        ),
                        requestFields(
                                fieldWithPath("title").description("일정 제목")
                                        .attributes(Attributes.key("constraint").value("최대 10자")),
                                fieldWithPath("todo").description("일정 할일")
                                        .attributes(Attributes.key("constraint").value("최대 200자"))
                        )
                ));
    }

    @DisplayName("유저는 일정을 삭제할 수 있다.")
    @Test
    void delete_schedule() throws Exception {
        Schedule schedule = getSchedule();
        User dummyUser = schedule.getUser();

        MockHttpSession dummySession = createDummySession(dummyUser.getId());

        mockMvc.perform(delete("/api/schedules/{scheduleId}", schedule.getId())
                        .session(dummySession))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("schedule-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("조회할 일정 번호")
                        )
                ));
    }

    private Schedule getSchedule() {
        return scheduleRepository.findAll().stream().findFirst().get();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).get();
    }

    private Long getUserId() {
        return userRepository.findByEmail("abc@naver.com").get().getId();
    }

    private MockHttpSession createDummySession(Long id) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("SESSION-KEY", id);
        return session;
    }
}