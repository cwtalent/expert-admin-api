package com.expert.admin.controller;

import com.expert.admin.dto.PageResult;
import com.expert.admin.dto.VoteDto;
import com.expert.admin.dto.VoteSearchDto;
import com.expert.admin.service.VoteService;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {
    
    @Autowired
    private VoteService service;
    
    @GetMapping
    public Map<String, Object> getAll(VoteSearchDto searchDto) {
        // 如果没有搜索条件，使用默认分页查询
        boolean hasSearchCondition = searchDto.getProjectTypeId() != null
            || searchDto.getStartedAtFrom() != null
            || searchDto.getStartedAtTo() != null
            || searchDto.getClosedAtFrom() != null
            || searchDto.getClosedAtTo() != null
            || searchDto.getRoundNumber() != null
            || (searchDto.getProjectName() != null && !searchDto.getProjectName().trim().isEmpty())
            || (searchDto.getExpertName() != null && !searchDto.getExpertName().trim().isEmpty())
            || (searchDto.getAwardLevel() != null && !searchDto.getAwardLevel().trim().isEmpty());
        
        PageResult<VoteDto> result;
        if (hasSearchCondition) {
            result = service.search(searchDto);
        } else {
            result = service.findAllWithPagination(
                searchDto.getPage() != null ? searchDto.getPage() : 1,
                searchDto.getPageSize() != null ? searchDto.getPageSize() : 20
            );
        }
        return ResponseUtil.success(result);
    }
    
    @GetMapping("/by-session/{sessionGuid}")
    public Map<String, Object> getBySession(@PathVariable String sessionGuid) {
        List<VoteDto> list = service.findBySessionGuid(sessionGuid);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/by-session/{sessionGuid}/round/{roundNumber}")
    public Map<String, Object> getBySessionAndRound(
            @PathVariable String sessionGuid,
            @PathVariable Integer roundNumber) {
        List<VoteDto> list = service.findBySessionAndRound(sessionGuid, roundNumber);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/by-project/{projectId}")
    public Map<String, Object> getByProject(@PathVariable Integer projectId) {
        List<VoteDto> list = service.findByProjectId(projectId);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/by-expert/{expertId}")
    public Map<String, Object> getByExpert(@PathVariable Integer expertId) {
        List<VoteDto> list = service.findByExpertId(expertId);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        // TODO: 实现统计功能
        return ResponseUtil.success("统计功能待实现");
    }
}
