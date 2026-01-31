package com.expert.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.admin.entity.Project;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProjectMapper extends BaseMapper<Project> {
    
    List<Project> findByProjectTypeId(@Param("projectTypeId") Integer projectTypeId);
    
    List<Project> findByProjectTypeIdAndStatus(@Param("projectTypeId") Integer projectTypeId, @Param("status") String status);
    
    IPage<Project> searchProjects(
        Page<Project> page,
        @Param("projectTypeId") Integer projectTypeId,
        @Param("projectName") String projectName,
        @Param("applicantName") String applicantName,
        @Param("applicantDepartment") String applicantDepartment,
        @Param("status") String status
    );
}
