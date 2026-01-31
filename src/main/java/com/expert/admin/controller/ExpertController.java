package com.expert.admin.controller;

import com.expert.admin.dto.ExpertDto;
import com.expert.admin.dto.ExpertSearchDto;
import com.expert.admin.dto.PageResult;
import com.expert.admin.service.ExpertService;
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

@RestController
@RequestMapping("/experts")
public class ExpertController {
    
    @Autowired
    private ExpertService service;
    
    @GetMapping
    public Map<String, Object> getAll(ExpertSearchDto searchDto) {
        PageResult<ExpertDto> result = service.search(searchDto);
        return ResponseUtil.success(result);
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        ExpertDto dto = service.findById(id);
        if (dto == null) {
            return ResponseUtil.error(404, "专家不存在");
        }
        return ResponseUtil.success(dto);
    }
    
    @GetMapping("/by-phone/{phone}")
    public Map<String, Object> getByPhone(@PathVariable String phone) {
        ExpertDto dto = service.findByPhone(phone);
        if (dto == null) {
            return ResponseUtil.error(404, "专家不存在");
        }
        return ResponseUtil.success(dto);
    }
    
    @PostMapping
    public Map<String, Object> create(@RequestBody ExpertDto dto) {
        try {
            ExpertDto result = service.create(dto);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Integer id, @RequestBody ExpertDto dto) {
        try {
            ExpertDto result = service.update(id, dto);
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
            Resource resource = ExcelUtil.generateExpertTemplate();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=专家导入模板.xlsx")
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
