package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpertDto {
    private Integer id;
    private String name;
    private String phone;
    private Boolean isActive;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
