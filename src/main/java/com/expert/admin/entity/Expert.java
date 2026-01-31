package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("experts")
@Data
public class Expert {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("name")
    private String name;
    
    @TableField("phone")
    private String phone;
    
    @TableField("is_active")
    private Boolean isActive = true;
    
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
