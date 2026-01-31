package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("expert_login_logs")
@Data
public class ExpertLoginLog {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField(exist = false)
    private Expert expert;
    
    @TableField("expert_id")
    private Integer expertId;
    
    @TableField("login_time")
    private LocalDateTime loginTime;
    
    @TableField("ip_address")
    private String ipAddress;
    
    @TableField("user_agent")
    private String userAgent;
    
    @TableField("login_status")
    private String loginStatus = "success";
    
    @TableField("failure_reason")
    private String failureReason;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
}
