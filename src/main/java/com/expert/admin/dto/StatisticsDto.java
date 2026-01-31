package com.expert.admin.dto;

import lombok.Data;
import java.util.Map;

@Data
public class StatisticsDto {
    private Long totalProjects;
    private Long totalExperts;
    private Long totalVotes;
    private Long activeSessions;
    private Map<String, Long> projectsByType;
    private Map<String, Long> votesByLevel;
    private Long totalLoginLogs;
}
