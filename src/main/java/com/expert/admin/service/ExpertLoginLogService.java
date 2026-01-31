package com.expert.admin.service;

import com.expert.admin.dto.ExpertLoginLogDto;
import com.expert.admin.dto.PageResult;
import com.expert.admin.entity.Expert;
import com.expert.admin.entity.ExpertLoginLog;
import com.expert.admin.mapper.ExpertLoginLogMapper;
import com.expert.admin.mapper.ExpertMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class ExpertLoginLogService {
    
    @Autowired
    private ExpertLoginLogMapper mapper;
    
    @Autowired
    private ExpertMapper expertMapper;
    
    public List<ExpertLoginLogDto> findAll() {
        return mapper.selectList(null).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public PageResult<ExpertLoginLogDto> findAllWithPagination(Integer page, Integer pageSize) {
        QueryWrapper<ExpertLoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("login_time");
        Page<ExpertLoginLog> pageObj = new Page<>(page, pageSize);
        IPage<ExpertLoginLog> result = mapper.selectPage(pageObj, queryWrapper);
        
        List<ExpertLoginLogDto> content = result.getRecords().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageResult<>(
            content,
            result.getTotal(),
            page,
            pageSize
        );
    }
    
    public List<ExpertLoginLogDto> findByExpertId(Integer expertId) {
        return mapper.findByExpertIdOrderByLoginTimeDesc(expertId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    private ExpertLoginLogDto toDto(ExpertLoginLog entity) {
        ExpertLoginLogDto dto = new ExpertLoginLogDto();
        BeanUtils.copyProperties(entity, dto);
        
        // 查询专家姓名
        if (entity.getExpertId() != null) {
            Expert expert = expertMapper.selectById(entity.getExpertId());
            if (expert != null) {
                dto.setExpertName(expert.getName());
            }
        }
        
        // 根据UA判断设备类型
        dto.setDevice(parseDeviceFromUserAgent(entity.getUserAgent()));
        
        return dto;
    }
    
    /**
     * 根据User-Agent判断设备类型
     */
    private String parseDeviceFromUserAgent(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "未知";
        }
        
        String ua = userAgent.toLowerCase();
        
        // 移动设备判断
        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") || ua.contains("ipad")) {
            if (ua.contains("iphone")) {
                return "iPhone";
            } else if (ua.contains("ipad")) {
                return "iPad";
            } else if (ua.contains("android")) {
                // 尝试提取Android版本
                if (ua.contains("tablet")) {
                    return "Android平板";
                } else {
                    return "Android手机";
                }
            } else {
                return "移动设备";
            }
        }
        
        // 桌面浏览器判断
        if (ua.contains("windows")) {
            if (ua.contains("edg")) {
                return "Windows - Edge";
            } else if (ua.contains("chrome")) {
                return "Windows - Chrome";
            } else if (ua.contains("firefox")) {
                return "Windows - Firefox";
            } else if (ua.contains("safari") && !ua.contains("chrome")) {
                return "Windows - Safari";
            } else {
                return "Windows";
            }
        } else if (ua.contains("macintosh") || ua.contains("mac os")) {
            if (ua.contains("safari") && !ua.contains("chrome")) {
                return "Mac - Safari";
            } else if (ua.contains("chrome")) {
                return "Mac - Chrome";
            } else if (ua.contains("firefox")) {
                return "Mac - Firefox";
            } else {
                return "Mac";
            }
        } else if (ua.contains("linux")) {
            if (ua.contains("chrome")) {
                return "Linux - Chrome";
            } else if (ua.contains("firefox")) {
                return "Linux - Firefox";
            } else {
                return "Linux";
            }
        }
        
        return "未知";
    }
}
