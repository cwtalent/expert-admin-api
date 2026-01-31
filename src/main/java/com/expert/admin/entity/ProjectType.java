package com.expert.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("project_types")
@Data
public class ProjectType {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("name")
    private String name;
    
    @TableField("code")
    private String code;
    
    @TableField("description")
    private String description;
    
    @TableField("sort_order")
    private Integer sortOrder = 0;
    
    @TableField("is_active")
    private Boolean isActive = true;
    
    @TableField("first_prize_count")
    private Integer firstPrizeCount;
    
    @TableField("second_prize_count")
    private Integer secondPrizeCount;
    
    @TableField("third_prize_count")
    private Integer thirdPrizeCount;
    
    @TableField("current_session_guid")
    private String currentSessionGuid;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
