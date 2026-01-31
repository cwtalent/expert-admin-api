package com.expert.admin.controller;

import com.expert.admin.dto.StatisticsDto;
import com.expert.admin.service.StatisticsService;
import com.expert.admin.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService service;
    
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        StatisticsDto dto = service.getOverview();
        return ResponseUtil.success(dto);
    }
    
    @GetMapping("/by-type/{typeId}")
    public Map<String, Object> getByType(@PathVariable Integer typeId) {
        StatisticsDto dto = service.getByType(typeId);
        return ResponseUtil.success(dto);
    }
}
