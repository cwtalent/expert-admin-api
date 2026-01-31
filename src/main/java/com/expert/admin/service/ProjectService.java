package com.expert.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.admin.dto.PageResult;
import com.expert.admin.dto.ProjectDto;
import com.expert.admin.dto.ProjectSearchDto;
import com.expert.admin.entity.Project;
import com.expert.admin.entity.ProjectType;
import com.expert.admin.mapper.ProjectMapper;
import com.expert.admin.mapper.ProjectTypeMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {
    
    @Autowired
    private ProjectMapper mapper;
    
    @Autowired
    private ProjectTypeMapper projectTypeMapper;
    
    public List<ProjectDto> findAll() {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_order").orderByAsc("id");
        return mapper.selectList(queryWrapper).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public PageResult<ProjectDto> search(ProjectSearchDto searchDto) {
        Page<Project> page = new Page<>(searchDto.getPage(), searchDto.getPageSize());
        
        // 将空字符串转换为 null，避免查询问题
        String projectName = (searchDto.getProjectName() != null && !searchDto.getProjectName().trim().isEmpty()) 
            ? searchDto.getProjectName().trim() : null;
        String applicantName = (searchDto.getApplicantName() != null && !searchDto.getApplicantName().trim().isEmpty()) 
            ? searchDto.getApplicantName().trim() : null;
        String applicantDepartment = (searchDto.getApplicantDepartment() != null && !searchDto.getApplicantDepartment().trim().isEmpty()) 
            ? searchDto.getApplicantDepartment().trim() : null;
        String status = (searchDto.getStatus() != null && !searchDto.getStatus().trim().isEmpty()) 
            ? searchDto.getStatus().trim() : null;
        
        IPage<Project> result = mapper.searchProjects(
            page,
            searchDto.getProjectTypeId(),
            projectName,
            applicantName,
            applicantDepartment,
            status
        );
        
        List<ProjectDto> content = result.getRecords().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageResult<>(
            content,
            result.getTotal(),
            searchDto.getPage(),
            searchDto.getPageSize()
        );
    }
    
    public void batchDelete(List<Integer> ids) {
        mapper.deleteBatchIds(ids);
    }
    
    public List<ProjectDto> findByTypeId(Integer typeId) {
        return mapper.findByProjectTypeId(typeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public ProjectDto findById(Integer id) {
        Project entity = mapper.selectById(id);
        if (entity == null) {
            return null;
        }
        return toDto(entity);
    }
    
    public ProjectDto create(ProjectDto dto) {
        Project entity = new Project();
        BeanUtils.copyProperties(dto, entity);
        
        // 确保 projectTypeId 被设置
        if (dto.getProjectTypeId() != null) {
            entity.setProjectTypeId(dto.getProjectTypeId());
            // 验证项目类型是否存在
            ProjectType projectType = projectTypeMapper.selectById(dto.getProjectTypeId());
            if (projectType == null) {
                throw new RuntimeException("项目类型不存在");
            }
        } else {
            throw new RuntimeException("项目类型ID不能为空");
        }
        
        mapper.insert(entity);
        return toDto(entity);
    }
    
    public ProjectDto update(Integer id, ProjectDto dto) {
        Project entity = mapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("项目不存在");
        }
        
        BeanUtils.copyProperties(dto, entity, "id", "createdAt", "projectTypeId");
        
        // 处理项目类型更新
        if (dto.getProjectTypeId() != null) {
            // 验证项目类型是否存在
            ProjectType projectType = projectTypeMapper.selectById(dto.getProjectTypeId());
            if (projectType == null) {
                throw new RuntimeException("项目类型不存在");
            }
            // 设置 projectTypeId（这是实际存储到数据库的字段）
            entity.setProjectTypeId(dto.getProjectTypeId());
        } else {
            throw new RuntimeException("项目类型ID不能为空");
        }
        
        mapper.updateById(entity);
        return toDto(entity);
    }
    
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
    
    public void batchImport(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            // 获取所有项目类型，建立名称到ID的映射
            Map<String, Integer> projectTypeMap = projectTypeMapper.selectList(null).stream()
                    .collect(Collectors.toMap(ProjectType::getName, ProjectType::getId));
            
            // 状态中文到英文的映射
            Map<String, String> statusMap = new HashMap<>();
            statusMap.put("草稿", "draft");
            statusMap.put("已提交", "submitted");
            statusMap.put("已批准", "approved");
            statusMap.put("已拒绝", "rejected");
            
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            // 从第2行开始读取（第1行是标题，第2行是示例数据）
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    // 读取项目类型名称（第0列）
                    Cell projectTypeCell = row.getCell(0);
                    if (projectTypeCell == null) continue;
                    String projectTypeName = getCellValueAsString(projectTypeCell);
                    if (projectTypeName == null || projectTypeName.trim().isEmpty()) continue;
                    
                    Integer projectTypeId = projectTypeMap.get(projectTypeName.trim());
                    if (projectTypeId == null) {
                        throw new RuntimeException("项目类型不存在: " + projectTypeName);
                    }
                    
                    // 读取排序号（第1列）
                    Cell sortOrderCell = row.getCell(1);
                    Integer sortOrder = null;
                    if (sortOrderCell != null) {
                        if (sortOrderCell.getCellType() == CellType.NUMERIC) {
                            sortOrder = (int) sortOrderCell.getNumericCellValue();
                        } else {
                            String sortOrderStr = getCellValueAsString(sortOrderCell);
                            if (sortOrderStr != null && !sortOrderStr.trim().isEmpty()) {
                                try {
                                    sortOrder = Integer.parseInt(sortOrderStr.trim());
                                } catch (NumberFormatException e) {
                                    // 忽略无效的排序号
                                }
                            }
                        }
                    }
                    
                    // 读取项目名称（第2列）
                    Cell projectNameCell = row.getCell(2);
                    String projectName = getCellValueAsString(projectNameCell);
                    if (projectName == null || projectName.trim().isEmpty()) {
                        throw new RuntimeException("项目名称不能为空");
                    }
                    
                    // 创建项目DTO
                    ProjectDto dto = new ProjectDto();
                    dto.setProjectTypeId(projectTypeId); // 必填字段
                    dto.setProjectName(projectName.trim()); // 必填字段
                    if (sortOrder != null) {
                        dto.setSortOrder(sortOrder);
                    }
                    
                    // 读取申报人姓名（第3列）
                    String applicantName = getCellValueAsString(row.getCell(3));
                    if (applicantName != null && !applicantName.trim().isEmpty()) {
                        dto.setApplicantName(applicantName.trim());
                    }
                    
                    // 读取申报部门（第4列）
                    String applicantDepartment = getCellValueAsString(row.getCell(4));
                    if (applicantDepartment != null && !applicantDepartment.trim().isEmpty()) {
                        dto.setApplicantDepartment(applicantDepartment.trim());
                    }
                    
                    // 读取主要完成人员（第5列）
                    String mainCompleters = getCellValueAsString(row.getCell(5));
                    if (mainCompleters != null && !mainCompleters.trim().isEmpty()) {
                        dto.setMainCompleters(mainCompleters.trim());
                    }
                    
                    // 读取主管总工（第6列）
                    String chiefEngineer = getCellValueAsString(row.getCell(6));
                    if (chiefEngineer != null && !chiefEngineer.trim().isEmpty()) {
                        dto.setChiefEngineer(chiefEngineer.trim());
                    }
                    
                    // 读取初评专家1（第7列）
                    String preliminaryExpert1Name = getCellValueAsString(row.getCell(7));
                    if (preliminaryExpert1Name != null && !preliminaryExpert1Name.trim().isEmpty()) {
                        dto.setPreliminaryExpert1Name(preliminaryExpert1Name.trim());
                    }
                    
                    // 读取初评等级1（第8列）
                    String preliminaryLevel1 = getCellValueAsString(row.getCell(8));
                    if (preliminaryLevel1 != null && !preliminaryLevel1.trim().isEmpty()) {
                        dto.setPreliminaryLevel1(preliminaryLevel1.trim());
                    }
                    
                    // 读取初评专家2（第9列）
                    String preliminaryExpert2Name = getCellValueAsString(row.getCell(9));
                    if (preliminaryExpert2Name != null && !preliminaryExpert2Name.trim().isEmpty()) {
                        dto.setPreliminaryExpert2Name(preliminaryExpert2Name.trim());
                    }
                    
                    // 读取初评等级2（第10列）
                    String preliminaryLevel2 = getCellValueAsString(row.getCell(10));
                    if (preliminaryLevel2 != null && !preliminaryLevel2.trim().isEmpty()) {
                        dto.setPreliminaryLevel2(preliminaryLevel2.trim());
                    }
                    
                    // 读取状态（第11列）
                    Cell statusCell = row.getCell(11);
                    String statusChinese = getCellValueAsString(statusCell);
                    if (statusChinese != null && !statusChinese.trim().isEmpty()) {
                        String status = statusMap.get(statusChinese.trim());
                        if (status == null) {
                            status = "draft"; // 默认值
                        }
                        dto.setStatus(status);
                    } else {
                        dto.setStatus("draft"); // 默认值
                    }
                    
                    // 保存项目
                    create(dto);
                    successCount++;
                    
                } catch (Exception e) {
                    failCount++;
                    errorMessages.append("第").append(i + 1).append("行: ").append(e.getMessage()).append("\n");
                }
            }
            
            workbook.close();
            inputStream.close();
            
            if (failCount > 0) {
                throw new RuntimeException(String.format("导入完成，成功: %d 条，失败: %d 条\n%s", 
                    successCount, failCount, errorMessages.toString()));
            }
            
        } catch (Exception e) {
            throw new RuntimeException("导入失败: " + e.getMessage(), e);
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字，避免科学计数法
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private ProjectDto toDto(Project entity) {
        ProjectDto dto = new ProjectDto();
        BeanUtils.copyProperties(entity, dto);
        // 根据 projectTypeId 查询项目类型名称
        if (entity.getProjectTypeId() != null) {
            ProjectType projectType = projectTypeMapper.selectById(entity.getProjectTypeId());
            if (projectType != null) {
                dto.setProjectTypeName(projectType.getName());
            }
        }
        return dto;
    }
}
