package com.expert.admin.service;

import com.expert.admin.dto.LoginDto;
import com.expert.admin.dto.LoginResponseDto;
import com.expert.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Tpxt@2026";
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public LoginResponseDto login(LoginDto loginDto) {
        if (ADMIN_USERNAME.equals(loginDto.getUsername()) && 
            ADMIN_PASSWORD.equals(loginDto.getPassword())) {
            String token = jwtUtil.generateToken(loginDto.getUsername());
            LoginResponseDto response = new LoginResponseDto();
            response.setToken(token);
            response.setUsername(loginDto.getUsername());
            return response;
        }
        throw new RuntimeException("用户名或密码错误");
    }
}
