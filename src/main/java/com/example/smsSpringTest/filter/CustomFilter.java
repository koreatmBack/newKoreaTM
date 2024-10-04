package com.example.smsSpringTest.filter;

import com.example.smsSpringTest.common.CachedBodyHttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Order(1)
public class CustomFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String contentType = request.getContentType();

        if(contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            final CachedBodyHttpServletRequest cachingRequest = new CachedBodyHttpServletRequest(request);
            final ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

            filterChain.doFilter(cachingRequest, cachingResponse);
            cachingResponse.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
