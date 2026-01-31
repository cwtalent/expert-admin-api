package com.expert.admin.controller;

import com.expert.admin.dto.ProjectTypeDto;
import com.expert.admin.service.ProjectTypeService;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projecttypes")
public class ProjectTypeController {
    
    @Autowired
    private ProjectTypeService service;
    
    @GetMapping
    public Map<String, Object> getAll() {
        List<ProjectTypeDto> list = service.findAll();
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/active")
    public Map<String, Object> getActive() {
        List<ProjectTypeDto> list = service.findActive();
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        ProjectTypeDto dto = service.findById(id);
        if (dto == null) {
            return ResponseUtil.error(404, "项目类型不存在");
        }
        return ResponseUtil.success(dto);
    }
    
    @PostMapping
    public Map<String, Object> create(@RequestBody ProjectTypeDto dto) {
        try {
            ProjectTypeDto result = service.create(dto);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody ProjectTypeDto dto) {
        try {
            ProjectTypeDto result = service.update(id, dto);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseUtil.success();
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/restart-voting")
    public Map<String, Object> restartVoting(@PathVariable Integer id) {
        try {
            ProjectTypeDto result = service.restartVoting(id);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}
