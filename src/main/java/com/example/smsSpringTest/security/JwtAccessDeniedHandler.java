//package com.example.smsSpringTest.security;
//
//import com.example.smsSpringTest.model.common.ApiResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//@Component
//public class JwtAccessDeniedHandler implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        ApiResponse apiResponse = new ApiResponse("E403", "접근 권한이 없습니다.");
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("utf-8");
//        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
//    }
//}
