package com.sparta.scheduleJpa.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scheduleJpa.global.exception.UnauthorizedException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import com.sparta.scheduleJpa.global.exception.error.ErrorResponse;
import com.sparta.scheduleJpa.global.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    // 이 URI들은 인증이 필요하지 않기 때문에 세션 검증을 하지 않음
    private static final String[] WHITE_LIST = {"/api/users/signup", "/api/users/login"};

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        // 다양한 메소드를 사용하기 위해 ServletRequest와 ServletResponse 형변환
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            // 화이트리스트에 포함되지 않은 URI일 경우 세션 검증을 통해 인증 여부 확인
            // GET 요청은 조회 기능들로 구성되어 있기 때문에 따로 인증을 하지 않도록 예외 처리, 다른 요청은 세션 검증이 필요
            if (!isWhiteList(requestURI) && !HttpMethod.GET.matches(httpRequest.getMethod())) {
                SessionUtil.validateSession(httpRequest.getSession(false));
            }
        } catch (UnauthorizedException e) {
            // 인증 실패 시, 401 Unauthorized 응답
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // HTTP 상태 코드 401 설정
            httpResponse.setContentType("application/json");  // 응답 타입을 JSON 형식으로 설정
            httpResponse.setCharacterEncoding("UTF-8");  // 응답 인코딩을 UTF-8로 설정

            // 오류 응답 객체 생성: 사용자에게 어떤 오류가 발생했는지 전달
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED.getStatus(), ErrorCode.UNAUTHORIZED.getMessage());

            // GlobalExceptionHandler 형식에 맞추기 위해 ObjectMapper를 사용하여 오류 응답을 JSON 형식으로 변환하여 클라이언트에게 반환
            ObjectMapper objectMapper = new ObjectMapper();
            httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));  // 응답 출력
            return;
        }

        chain.doFilter(request, response);
    }

    // 요청 URI가 화이트리스트에 포함되어 있는지 확인하는 메소드
    // 화이트리스트에 포함된 URI는 인증 없이 접근을 허용하기 위해 사용
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
