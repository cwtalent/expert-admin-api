package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.admin.entity.ProjectType;
import java.util.List;

public interface ProjectTypeMapper extends BaseMapper<ProjectType> {
    
    List<ProjectType> findActiveOrderBySortOrder();
}
