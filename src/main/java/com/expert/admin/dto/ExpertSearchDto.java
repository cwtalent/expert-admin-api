package com.expert.admin.dto;

import lombok.Data;

@Data
public class ExpertSearchDto {
    private String name;
    private String phone;
    private Boolean isActive;
    private Integer page = 1;
    private Integer pageSize = 20;
}
