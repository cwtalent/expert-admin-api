package com.expert.admin.service;

import com.expert.admin.dto.ProjectTypeDto;
import com.expert.admin.entity.ProjectType;
import com.expert.admin.entity.VotingSession;
import com.expert.admin.mapper.ProjectTypeMapper;
import com.expert.admin.mapper.VotingSessionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectTypeService {
    
    @Autowired
    private ProjectTypeMapper mapper;
    
    @Autowired
    private VotingSessionMapper votingSessionMapper;
    
    public List<ProjectTypeDto> findAll() {
        return mapper.selectList(null).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<ProjectTypeDto> findActive() {
        return mapper.findActiveOrderBySortOrder().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public ProjectTypeDto findById(Integer id) {
        ProjectType entity = mapper.selectById(id);
        if (entity == null) {
            return null;
        }
        return toDto(entity);
    }
    
    public ProjectTypeDto create(ProjectTypeDto dto) {
        ProjectType entity = new ProjectType();
        BeanUtils.copyProperties(dto, entity);
        mapper.insert(entity);
        return toDto(entity);
    }
    
    public ProjectTypeDto update(Integer id, ProjectTypeDto dto) {
        ProjectType entity = mapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("项目类型不存在");
        }
        BeanUtils.copyProperties(dto, entity, "id", "createdAt");
        mapper.updateById(entity);
        return toDto(entity);
    }
    
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
    
    public ProjectTypeDto restartVoting(Integer projectTypeId) {
        ProjectType entity = mapper.selectById(projectTypeId);
        if (entity == null) {
            throw new RuntimeException("项目类型不存在");
        }
        
        // 关闭该项目类型下旧的 active/pending 会话
        List<VotingSession> oldSessions = votingSessionMapper.findByProjectTypeId(projectTypeId);
        for (VotingSession oldSession : oldSessions) {
            if ("active".equals(oldSession.getStatus()) || "pending".equals(oldSession.getStatus())) {
                oldSession.setStatus("closed");
                oldSession.setClosedAt(LocalDateTime.now());
                votingSessionMapper.updateById(oldSession);
            }
        }
        
        // 创建新的投票会话
        VotingSession newSession = new VotingSession();
        newSession.setSessionGuid(UUID.randomUUID().toString());
        newSession.setProjectTypeId(projectTypeId);
        newSession.setStatus("pending");
        
        // 生成投票URL（需要根据实际前端地址配置）
        String baseUrl = "http://localhost:3002"; // 可以从配置读取
        String votingUrl = baseUrl + "/voting/" + newSession.getSessionGuid();
        newSession.setVotingUrl(votingUrl);
        
        newSession.setCreatedAt(LocalDateTime.now());
        newSession.setUpdatedAt(LocalDateTime.now());
        votingSessionMapper.insert(newSession);
        
        // 更新 project_types.current_session_guid
        entity.setCurrentSessionGuid(newSession.getSessionGuid());
        mapper.updateById(entity);
        
        return toDto(entity);
    }
    
    private ProjectTypeDto toDto(ProjectType entity) {
        ProjectTypeDto dto = new ProjectTypeDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
