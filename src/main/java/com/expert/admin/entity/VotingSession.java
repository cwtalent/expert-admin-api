package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("voting_sessions")
@Data
public class VotingSession {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("session_guid")
    private String sessionGuid;
    
    @TableField(exist = false)
    private ProjectType projectType;
    
    @TableField("project_type_id")
    private Integer projectTypeId;
    
    @TableField("qr_code_url")
    private String qrCodeUrl;
    
    @TableField("voting_url")
    private String votingUrl;
    
    @TableField("status")
    private String status = "pending";
    
    @TableField("started_at")
    private LocalDateTime startedAt;
    
    @TableField("closed_at")
    private LocalDateTime closedAt;
    
    @TableField("created_by")
    private String createdBy;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
