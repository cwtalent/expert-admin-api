package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.admin.entity.Expert;
import org.apache.ibatis.annotations.Param;

public interface ExpertMapper extends BaseMapper<Expert> {
    
    Expert findByPhone(@Param("phone") String phone);
    
    IPage<Expert> searchExperts(
        Page<Expert> page,
        @Param("name") String name,
        @Param("phone") String phone,
        @Param("isActive") Boolean isActive
    );
}
