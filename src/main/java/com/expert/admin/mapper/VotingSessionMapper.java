package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.expert.admin.entity.VotingSession;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface VotingSessionMapper extends BaseMapper<VotingSession> {
    
    VotingSession findBySessionGuid(@Param("sessionGuid") String sessionGuid);
    
    List<VotingSession> findByProjectTypeId(@Param("projectTypeId") Integer projectTypeId);
    
    List<VotingSession> findByStatus(@Param("status") String status);
}
