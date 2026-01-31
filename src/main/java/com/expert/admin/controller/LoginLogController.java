package com.expert.admin.controller;

import com.expert.admin.dto.ExpertLoginLogDto;
import com.expert.admin.dto.PageResult;
import com.expert.admin.service.ExpertLoginLogService;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loginlogs")
public class LoginLogController {
    
    @Autowired
    private ExpertLoginLogService service;
    
    @GetMapping
    public Map<String, Object> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResult<ExpertLoginLogDto> result = service.findAllWithPagination(page, pageSize);
        return ResponseUtil.success(result);
    }
    
    @GetMapping("/expert/{expertId}")
    public Map<String, Object> getByExpertId(@PathVariable Integer expertId) {
        List<ExpertLoginLogDto> list = service.findByExpertId(expertId);
        return ResponseUtil.success(list);
    }
}
