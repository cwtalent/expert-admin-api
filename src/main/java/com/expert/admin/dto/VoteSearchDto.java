package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoteSearchDto {
    private Integer projectTypeId;  // 项目类型ID
    private LocalDateTime startedAtFrom;  // 开始投票时间（起始）
    private LocalDateTime startedAtTo;    // 开始投票时间（结束）
    private LocalDateTime closedAtFrom;   // 结束投票时间（起始）
    private LocalDateTime closedAtTo;     // 结束投票时间（结束）
    private Integer roundNumber;          // 轮次
    private String projectName;           // 项目名称
    private String expertName;            // 专家姓名
    private String awardLevel;            // 奖级
    private Integer page = 1;
    private Integer pageSize = 20;
}
