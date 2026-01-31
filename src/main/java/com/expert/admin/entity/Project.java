package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("projects")
@Data
public class Project {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField(exist = false)
    private ProjectType projectType;
    
    @TableField("project_type_id")
    private Integer projectTypeId;
    
    @TableField("applicant_name")
    private String applicantName;
    
    @TableField("applicant_department")
    private String applicantDepartment;
    
    @TableField("project_name")
    private String projectName;
    
    @TableField("main_completers")
    private String mainCompleters;
    
    @TableField("chief_engineer")
    private String chiefEngineer;
    
    @TableField("preliminary_expert1_name")
    private String preliminaryExpert1Name;
    
    @TableField("preliminary_level1")
    private String preliminaryLevel1;
    
    @TableField("preliminary_expert2_name")
    private String preliminaryExpert2Name;
    
    @TableField("preliminary_level2")
    private String preliminaryLevel2;
    
    @TableField("status")
    private String status = "draft";
    
    @TableField("sort_order")
    private Integer sortOrder = 0;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
