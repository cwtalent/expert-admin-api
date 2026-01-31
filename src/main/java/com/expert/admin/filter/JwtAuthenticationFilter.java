package com.expert.admin.filter;

import com.expert.admin.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/api/auth/login",
        "/api/swagger-ui",
        "/api/api-docs",
        "/api/swagger-ui.html"
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // 排除登录接口和Swagger接口
        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String token = getTokenFromRequest(request);
        
        if (token == null || !jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权或token已过期\",\"data\":null}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
