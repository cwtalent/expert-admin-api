package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.admin.entity.Vote;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface VoteMapper extends BaseMapper<Vote> {
    
    List<Vote> findBySessionGuid(@Param("sessionGuid") String sessionGuid);
    
    List<Vote> findBySessionGuidAndRoundNumber(@Param("sessionGuid") String sessionGuid, @Param("roundNumber") Integer roundNumber);
    
    List<Vote> findByProjectId(@Param("projectId") Integer projectId);
    
    List<Vote> findByExpertId(@Param("expertId") Integer expertId);
    
    List<Vote> findByProjectTypeId(@Param("projectTypeId") Integer projectTypeId);
    
    Long countDistinctExpertsBySessionAndRound(@Param("sessionGuid") String sessionGuid, @Param("roundNumber") Integer roundNumber);
    
    IPage<Vote> findAllWithPagination(Page<Vote> page);
    
    IPage<Vote> searchVotes(
        Page<Vote> page,
        @Param("projectTypeId") Integer projectTypeId,
        @Param("startedAtFrom") java.time.LocalDateTime startedAtFrom,
        @Param("startedAtTo") java.time.LocalDateTime startedAtTo,
        @Param("closedAtFrom") java.time.LocalDateTime closedAtFrom,
        @Param("closedAtTo") java.time.LocalDateTime closedAtTo,
        @Param("roundNumber") Integer roundNumber,
        @Param("projectName") String projectName,
        @Param("expertName") String expertName,
        @Param("awardLevel") String awardLevel
    );
}
