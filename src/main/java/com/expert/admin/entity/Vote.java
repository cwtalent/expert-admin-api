package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("votes")
@Data
public class Vote {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("session_guid")
    private String sessionGuid;
    
    @TableField(exist = false)
    private ProjectType projectType;
    
    @TableField("project_type_id")
    private Integer projectTypeId;
    
    @TableField("round_number")
    private Integer roundNumber = 1;
    
    @TableField(exist = false)
    private Project project;
    
    @TableField("project_id")
    private Integer projectId;
    
    @TableField(exist = false)
    private Expert expert;
    
    @TableField("expert_id")
    private Integer expertId;
    
    @TableField("award_level")
    private String awardLevel;
    
    @TableField("vote_time")
    private LocalDateTime voteTime;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
}
