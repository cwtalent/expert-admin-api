package com.expert.admin.service;

import com.expert.admin.dto.StatisticsDto;
import com.expert.admin.entity.ProjectType;
import com.expert.admin.entity.Vote;
import com.expert.admin.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatisticsService {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private ExpertMapper expertMapper;
    
    @Autowired
    private VoteMapper voteMapper;
    
    @Autowired
    private VotingSessionMapper votingSessionMapper;
    
    @Autowired
    private ExpertLoginLogMapper loginLogMapper;
    
    @Autowired
    private ProjectTypeMapper projectTypeMapper;
    
    public StatisticsDto getOverview() {
        StatisticsDto dto = new StatisticsDto();
        
        dto.setTotalProjects((long) projectMapper.selectCount(null));
        dto.setTotalExperts((long) expertMapper.selectCount(null));
        dto.setTotalVotes((long) voteMapper.selectCount(null));
        dto.setActiveSessions((long) votingSessionMapper.findByStatus("active").size());
        dto.setTotalLoginLogs((long) loginLogMapper.selectCount(null));
        
        // 按类型统计项目数
        Map<String, Long> projectsByType = new HashMap<>();
        List<ProjectType> types = projectTypeMapper.selectList(null);
        for (ProjectType type : types) {
            long count = projectMapper.findByProjectTypeId(type.getId()).size();
            projectsByType.put(type.getName(), count);
        }
        dto.setProjectsByType(projectsByType);
        
        // 按奖级统计投票数
        Map<String, Long> votesByLevel = voteMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(Vote::getAwardLevel, Collectors.counting()));
        dto.setVotesByLevel(votesByLevel);
        
        return dto;
    }
    
    public StatisticsDto getByType(Integer typeId) {
        StatisticsDto dto = new StatisticsDto();
        
        dto.setTotalProjects((long) projectMapper.findByProjectTypeId(typeId).size());
        dto.setTotalVotes((long) voteMapper.findByProjectTypeId(typeId).size());
        
        return dto;
    }
}
