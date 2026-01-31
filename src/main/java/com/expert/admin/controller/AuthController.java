package com.expert.admin.controller;

import com.expert.admin.dto.LoginDto;
import com.expert.admin.dto.LoginResponseDto;
import com.expert.admin.service.AuthService;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDto loginDto) {
        try {
            LoginResponseDto response = authService.login(loginDto);
            return ResponseUtil.success(response);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}
