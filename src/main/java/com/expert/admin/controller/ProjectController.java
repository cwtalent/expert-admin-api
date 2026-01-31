package com.expert.admin.controller;

import com.expert.admin.dto.PageResult;
import com.expert.admin.dto.ProjectDto;
import com.expert.admin.dto.ProjectSearchDto;
import com.expert.admin.dto.ProjectTypeDto;
import com.expert.admin.service.ProjectService;
import com.expert.admin.service.ProjectTypeService;
import com.expert.admin.util.ExcelUtil;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService service;
    
    @Autowired
    private ProjectTypeService projectTypeService;
    
    @GetMapping
    public Map<String, Object> getAll(ProjectSearchDto searchDto) {
        PageResult<ProjectDto> result = service.search(searchDto);
        return ResponseUtil.success(result);
    }
    
    @GetMapping("/by-type/{typeId}")
    public Map<String, Object> getByType(@PathVariable Integer typeId) {
        List<ProjectDto> list = service.findByTypeId(typeId);
        return ResponseUtil.success(list);
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        ProjectDto dto = service.findById(id);
        if (dto == null) {
            return ResponseUtil.error(404, "项目不存在");
        }
        return ResponseUtil.success(dto);
    }
    
    @PostMapping
    public Map<String, Object> create(@RequestBody ProjectDto dto) {
        try {
            ProjectDto result = service.create(dto);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody ProjectDto dto) {
        try {
            ProjectDto result = service.update(id, dto);
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
    
    @PostMapping("/batch-delete")
    public Map<String, Object> batchDelete(@RequestBody List<Integer> ids) {
        try {
            service.batchDelete(ids);
            return ResponseUtil.success("批量删除成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        try {
            // 获取所有项目类型名称
            List<ProjectTypeDto> projectTypes = projectTypeService.findActive();
            List<String> projectTypeNames = projectTypes.stream()
                    .map(ProjectTypeDto::getName)
                    .collect(Collectors.toList());
            
            Resource resource = ExcelUtil.generateProjectTemplate(projectTypeNames);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=项目导入模板.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("生成模板失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/batch-import")
    public Map<String, Object> batchImport(@RequestParam("file") MultipartFile file) {
        try {
            service.batchImport(file);
            return ResponseUtil.success("批量导入成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}
