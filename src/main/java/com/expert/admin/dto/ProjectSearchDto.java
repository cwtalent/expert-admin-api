package com.expert.admin.dto;

import lombok.Data;

@Data
public class ProjectSearchDto {
    private Integer projectTypeId;
    private String projectName;
    private String applicantName;
    private String applicantDepartment;
    private String status;
    private Integer page = 1;
    private Integer pageSize = 20;
}
