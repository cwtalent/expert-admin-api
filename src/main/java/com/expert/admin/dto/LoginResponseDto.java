package com.expert.admin.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String username;
}
