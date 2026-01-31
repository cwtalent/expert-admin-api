package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoteDto {
    private Integer id;
    private String sessionGuid;
    private Integer projectTypeId;
    private String projectTypeName;  // 项目类型名称
    private Integer roundNumber;
    private Integer projectId;
    private String projectName;
    private Integer expertId;
    private String expertName;
    private String awardLevel;
    private LocalDateTime voteTime;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;  // 投票会话开始时间
    private LocalDateTime closedAt;   // 投票会话结束时间
}
