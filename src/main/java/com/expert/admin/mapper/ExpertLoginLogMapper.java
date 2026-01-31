package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.admin.entity.ExpertLoginLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ExpertLoginLogMapper extends BaseMapper<ExpertLoginLog> {
    
    List<ExpertLoginLog> findByExpertIdOrderByLoginTimeDesc(@Param("expertId") Integer expertId);
}
