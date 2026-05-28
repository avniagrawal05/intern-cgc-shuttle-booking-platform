package com.hms.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.Instant;

/**
 * Interceptor for logging HTTP requests and responses
 * Tracks request processing time and logs request details
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTRIBUTE = "requestStartTime";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Instant startTime = Instant.now();
        String requestId = generateRequestId();
        
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        
        log.info("[{}] {} {} - Started", 
                requestId,
                request.getMethod(), 
                request.getRequestURI());
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) {
        // No-op
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        Instant startTime = (Instant) request.getAttribute(START_TIME_ATTRIBUTE);
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
        
        if (startTime != null) {
            Duration duration = Duration.between(startTime, Instant.now());
            
            if (ex != null) {
                log.error("[{}] {} {} - Failed after {}ms - Error: {}", 
                        requestId,
                        request.getMethod(), 
                        request.getRequestURI(),
                        duration.toMillis(),
                        ex.getMessage());
            } else {
                log.info("[{}] {} {} - Completed with status {} in {}ms", 
                        requestId,
                        request.getMethod(), 
                        request.getRequestURI(),
                        response.getStatus(),
                        duration.toMillis());
            }
        }
    }
    
    private String generateRequestId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
