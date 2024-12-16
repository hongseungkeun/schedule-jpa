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

    private static final String[] WHITE_LIST = {"/api/users/signup", "/api/users/login"};

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if (!isWhiteList(requestURI) && !HttpMethod.GET.matches(httpRequest.getMethod())) {
                SessionUtil.validateSession(httpRequest.getSession(false));
            }
        } catch (UnauthorizedException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED.getStatus(), ErrorCode.UNAUTHORIZED.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
