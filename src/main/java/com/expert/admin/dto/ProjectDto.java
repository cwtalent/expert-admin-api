package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectDto {
    private Integer id;
    private Integer projectTypeId;
    private String projectTypeName;
    private String applicantName;
    private String applicantDepartment;
    private String projectName;
    private String mainCompleters;
    private String chiefEngineer;
    private String preliminaryExpert1Name;
    private String preliminaryLevel1;
    private String preliminaryExpert2Name;
    private String preliminaryLevel2;
    private String status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
