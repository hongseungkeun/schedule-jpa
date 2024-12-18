package com.sparta.scheduleJpa.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scheduleJpa.domain.user.dto.request.UserLoginReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserSignUpReq;
import com.sparta.scheduleJpa.domain.user.dto.request.UserUpdateReq;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserSignUpReq request = new UserSignUpReq("abc@naver.com", "12345678", "테스트");
        userRepository.save(request.toEntity(passwordEncoder.encode(request.password())));
    }

    @Test
    @DisplayName("유저는 회원가입 할 수 있다")
    void user_signUp() throws Exception {
        UserSignUpReq request = new UserSignUpReq("abc1@naver.com", "12345678", "테스트");

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("user-signUp",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("유저 이메일")
                                        .attributes(Attributes.key("constraint").value("이메일 형식")),
                                fieldWithPath("password").description("유저 패스워드")
                                        .attributes(Attributes.key("constraint").value("최소 8자")),
                                fieldWithPath("name").description("유저 이름")
                                        .attributes(Attributes.key("constraint").value("최대 4자"))
                        )
                ));
    }

    @Test
    @DisplayName("이메일이 이미 존재한다면 회원가입 실패")
    void user_signUp_fail() throws Exception {
        UserSignUpReq request = new UserSignUpReq("abc@naver.com", "12345678", "테스트");

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.httpStatus").value("CONFLICT"))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print())
                .andDo(document("user-signUp-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("httpStatus").description("HTTP 상태 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )
                ));

    }

    @Test
    @DisplayName("유저는 로그인 할 수 있다")
    void user_login() throws Exception {
        UserLoginReq loginReq = new UserLoginReq("abc@naver.com", "12345678");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("유저 이메일")
                                        .attributes(Attributes.key("constraint").value("이메일 형식")),
                                fieldWithPath("password").description("유저 패스워드")
                                        .attributes(Attributes.key("constraint").value("최소 8자"))
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호를 틀릴 시 로그인 실패")
    void user_login_fail() throws Exception {
        UserLoginReq loginReq = new UserLoginReq("abc@naver.com", "123456789");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.httpStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print())
                .andDo(document("user-login-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("httpStatus").description("HTTP 상태 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("유저는 유저의 프로필을 볼 수 있다.")
    void user_find() throws Exception {
        mockMvc.perform(get("/api/users/{userId}", getUserId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").description("유저 식별자"),
                                fieldWithPath("name").description("유저 이름")
                                        .attributes(Attributes.key("constraint").value("최소 4자")),
                                fieldWithPath("email").description("유저 이메일")
                                        .attributes(Attributes.key("constraint").value("이메일 형식"))
                        )
                ));
    }

    @Test
    @DisplayName("유저는 자신의 정보를 수정할 수 있다.")
    void user_update() throws Exception {
        MockHttpSession dummySession = createDummySession(getUserId());

        UserUpdateReq request = new UserUpdateReq("12345678", "변경");
        mockMvc.perform(patch("/api/users/{userId}", getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .session(dummySession))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 식별자")
                        ),
                        requestFields(
                                fieldWithPath("password").description("유저 패스워드")
                                        .attributes(Attributes.key("constraint").value("최소 8자")),
                                fieldWithPath("name").description("유저 이름")
                                        .attributes(Attributes.key("constraint").value("최대 4자"))
                        )
                ));
    }

    @Test
    @DisplayName("유저는 자신을 삭제할 수 있다.")
    void user_delete() throws Exception {
        MockHttpSession dummySession = createDummySession(getUserId());

        mockMvc.perform(delete("/api/users/{userId}", getUserId())
                        .session(dummySession))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("user-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 식별자")
                        )
                ));
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