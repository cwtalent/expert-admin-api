package com.expert.admin.service;

import com.expert.admin.dto.PageResult;
import com.expert.admin.dto.VoteDto;
import com.expert.admin.dto.VoteSearchDto;
import com.expert.admin.entity.Expert;
import com.expert.admin.entity.Project;
import com.expert.admin.entity.ProjectType;
import com.expert.admin.entity.Vote;
import com.expert.admin.entity.VotingSession;
import com.expert.admin.mapper.ExpertMapper;
import com.expert.admin.mapper.ProjectMapper;
import com.expert.admin.mapper.ProjectTypeMapper;
import com.expert.admin.mapper.VoteMapper;
import com.expert.admin.mapper.VotingSessionMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoteService {
    
    @Autowired
    private VoteMapper mapper;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private ExpertMapper expertMapper;
    
    @Autowired
    private VotingSessionMapper votingSessionMapper;
    
    @Autowired
    private ProjectTypeMapper projectTypeMapper;
    
    public List<VoteDto> findAll() {
        return mapper.selectList(null).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public PageResult<VoteDto> findAllWithPagination(Integer page, Integer pageSize) {
        Page<Vote> pageObj = new Page<>(page, pageSize);
        IPage<Vote> result = mapper.findAllWithPagination(pageObj);
        
        List<VoteDto> content = result.getRecords().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageResult<>(
            content,
            result.getTotal(),
            page,
            pageSize
        );
    }
    
    public PageResult<VoteDto> search(VoteSearchDto searchDto) {
        Page<Vote> pageObj = new Page<>(searchDto.getPage(), searchDto.getPageSize());
        
        // 将空字符串转换为 null，避免查询问题
        String projectName = (searchDto.getProjectName() != null && !searchDto.getProjectName().trim().isEmpty()) 
            ? searchDto.getProjectName().trim() : null;
        String expertName = (searchDto.getExpertName() != null && !searchDto.getExpertName().trim().isEmpty()) 
            ? searchDto.getExpertName().trim() : null;
        String awardLevel = (searchDto.getAwardLevel() != null && !searchDto.getAwardLevel().trim().isEmpty()) 
            ? searchDto.getAwardLevel().trim() : null;
        
        IPage<Vote> result = mapper.searchVotes(
            pageObj,
            searchDto.getProjectTypeId(),
            searchDto.getStartedAtFrom(),
            searchDto.getStartedAtTo(),
            searchDto.getClosedAtFrom(),
            searchDto.getClosedAtTo(),
            searchDto.getRoundNumber(),
            projectName,
            expertName,
            awardLevel
        );
        
        List<VoteDto> content = result.getRecords().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageResult<>(
            content,
            result.getTotal(),
            searchDto.getPage(),
            searchDto.getPageSize()
        );
    }
    
    public List<VoteDto> findBySessionGuid(String sessionGuid) {
        return mapper.findBySessionGuid(sessionGuid).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<VoteDto> findBySessionAndRound(String sessionGuid, Integer roundNumber) {
        return mapper.findBySessionGuidAndRoundNumber(sessionGuid, roundNumber).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<VoteDto> findByProjectId(Integer projectId) {
        return mapper.findByProjectId(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<VoteDto> findByExpertId(Integer expertId) {
        return mapper.findByExpertId(expertId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<VoteDto> findByProjectTypeId(Integer projectTypeId) {
        return mapper.findByProjectTypeId(projectTypeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    private VoteDto toDto(Vote entity) {
        VoteDto dto = new VoteDto();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getProjectId() != null) {
            Project project = projectMapper.selectById(entity.getProjectId());
            if (project != null) {
                dto.setProjectName(project.getProjectName());
            }
        }
        if (entity.getExpertId() != null) {
            Expert expert = expertMapper.selectById(entity.getExpertId());
            if (expert != null) {
                dto.setExpertName(expert.getName());
            }
        }
        // 查询项目类型名称
        if (entity.getProjectTypeId() != null) {
            ProjectType projectType = projectTypeMapper.selectById(entity.getProjectTypeId());
            if (projectType != null) {
                dto.setProjectTypeName(projectType.getName());
            }
        }
        // 查询投票会话的开始和结束时间
        if (entity.getSessionGuid() != null) {
            VotingSession session = votingSessionMapper.findBySessionGuid(entity.getSessionGuid());
            if (session != null) {
                dto.setStartedAt(session.getStartedAt());
                dto.setClosedAt(session.getClosedAt());
            }
        }
        return dto;
    }
}
